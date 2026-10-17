# Java Serialization — what it is and how we handle it

## What it is

Java serialization turns a live object into a byte stream (`ObjectOutputStream`) and back into an
object (`ObjectInputStream`). The stream works anywhere: memory, file, network. Same code, different
underlying stream.

```java
ByteArrayOutputStream bos = new ByteArrayOutputStream();
try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
    out.writeObject(obj);          // object -> bytes
}
try (ObjectInputStream in =
         new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
    Object copy = in.readObject(); // bytes -> object
}
```

Requirements: the class must `implements Serializable` (otherwise `NotSerializableException`),
and every field must be serializable too (fields are serialized recursively), or marked `transient`.

## What the JVM actually does

**Saving (`writeObject`):**

1. Checks the class implements `Serializable`.
2. Writes class metadata: class name, `serialVersionUID`, field list.
3. Writes every **non-static, non-transient** field's value.
   - `static` fields are skipped — they belong to the class, not the object.
   - `transient` fields are skipped — you marked them "don't persist".
4. Object-valued fields are serialized recursively.

**Loading (`readObject`):**

1. Reads the class metadata and compares `serialVersionUID` with the class on the classpath.
   Mismatch → `InvalidClassException`.
2. Allocates the object **without running any constructor** — not even private ones.
3. Copies field values from the stream in.
4. Then looks for a method with exactly this signature and, if present, calls it and uses its
   return value **instead of** the object it just built:

```java
private Object readResolve() {
    return theRealInstance;
}
```

## The critical fact

**Serialization never runs a constructor.** The JVM allocates memory and copies field values in
directly. This is why a private constructor and a reflection guard do nothing against it — the
guard lives in the constructor, and the constructor is never visited.

## How we protect a singleton

A singleton needs two defences, because serialization and reflection attack through different holes:

| Attack | Which hole | Defence | Why it works |
|---|---|---|---|
| Reflection | **always runs the constructor** | guard inside the constructor: `if (INSTANCE != null) throw` | first legit call passes (INSTANCE is still null while it runs); every later construction throws |
| Serialization | **never runs the constructor** | `private Object readResolve()` returning the real instance | JVM calls it after deserializing and uses its return value, discarding the fresh copy |

```java
public class IdGenerator implements Serializable {
    private static final long serialVersionUID = 1L;

    private IdGenerator() {
        if (INSTANCE != null) {              // reflection guard
            throw new IllegalStateException("Use getIdGenerator()");
        }
    }

    private Object readResolve() {           // serialization guard
        return getIdGenerator();             // accessor, not the field — covers the
    }                                        // "fresh JVM, nothing exists yet" edge
}
```

`readResolve` is `private` because it's a JVM hook, not an API — the JVM finds it via reflection
inside the same class. Returning the accessor instead of the field directly covers the edge case
where you deserialize into a JVM where the singleton doesn't exist yet.

Note the ordering that makes the reflection guard work: during the happy path, the constructor
runs **before** `INSTANCE` is assigned, so the guard sees `null` exactly once.

## serialVersionUID

- Written into the stream at save, compared at load.
- If you don't declare one, the JVM computes it from the class shape (field names, types, layout).
  Add one field later → the computed value changes → old saved data becomes unreadable.
- Declaring it pins the version: `private static final long serialVersionUID = 1L;`

## What deserialization does to state

Static fields are never in the stream — they belong to the class. Instance fields are copied in,
which is why a deserialized copy carries its state (a counter, a timestamp). With `readResolve`
the copy is discarded, so the real instance keeps its state — the counter never resets. Usually
that's exactly what you want.

## Honest caveats

- `readResolve` is the textbook defence. Other bypasses exist (`sun.misc.Unsafe.allocateInstance`
  skips both constructors and hooks) — out of scope for application code, but know they exist.
- In production, an **enum singleton** is JVM-guaranteed against serialization and reflection
  attacks with zero defensive code. For anything else, the holder idiom plus these guards is the
  standard toolkit.
