# Design Pattern Practice Set

Oct 5, 2026 · @me weird

## How to Use

107 problems across 30 sections, ordered by how often each pattern shows up in LLD rounds. Work top to bottom.

- **Each problem** is a short spec plus a **Twist**. The twist is the real test: it checks whether your design bends without breaking.
- **Done** means it compiles, runs from a `main`, and has at least one test proving the twist works.
- **Java, no frameworks.** Fake anything external (network, email, disk, exchange). The design is the point, not the I/O.
- **Problem count tracks importance:** 5 for heavy-hitters like Strategy and State, 2 for narrow ones like Null Object.
- **Four patterns added** for your fintech profile: Dependency Injection, Repository, Producer-Consumer, Object Pool.
- **The Mixed Round names no pattern.** Choosing one is the interview skill. Write one line on *why* before coding.

## 0. SOLID Principles

Each problem describes a broken design. Refactor it, keeping the public behaviour identical.

### 0.1 Single Responsibility — Invoice service

- `InvoiceService` calculates totals with tax, saves invoices to a database, emails a PDF to the customer, and writes an audit log.
- Split it so each class has exactly one reason to change.
- `createInvoice(order)` must still do all four things.
- **Twist:** Finance changes tax rules every quarter, and the email provider is being swapped next month. Show which single class each change touches.

### 0.2 Open/Closed — Discount calculator

- `DiscountCalculator.calculate(customer, amount)` has an if-else chain for REGULAR, PREMIUM and EMPLOYEE customers.
- Refactor so adding a customer type requires no edits to existing classes.
- Support at least four customer types.
- **Twist:** Add a festival sale that stacks on top of any customer discount, without touching any existing discount class.

### 0.3 Liskov Substitution — Bank accounts

- `BankAccount` has `deposit()` and `withdraw()`.
- `FixedDepositAccount extends BankAccount` throws on `withdraw()` before maturity.
- A loop that withdraws monthly fees from `List<BankAccount>` now crashes.
- Redesign the hierarchy so no subtype breaks what callers expect.
- **Twist:** Add a `LoanAccount` whose balance can go negative. It must fit the hierarchy with no special-casing in callers.

### 0.4 Interface Segregation — Smart devices

- One `SmartDevice` interface has `turnOn`, `turnOff`, `setTemperature`, `playMusic` and `recordVideo`.
- `SmartBulb` is forced to implement methods it cannot support.
- Split the interface so each device implements only what it can actually do.
- **Twist:** A home controller must "turn everything off" without knowing any device's concrete type.

### 0.5 Dependency Inversion — Order notifications

- `OrderService.placeOrder()` does `new SmtpEmailSender()` internally.
- Invert the dependency so `OrderService` depends on an abstraction.
- Unit-test `OrderService` without sending a real email.
- **Twist:** High-value orders go by SMS, others by email, chosen at startup from config, with zero changes to `OrderService`.

## 1. Strategy

### 1.1 Payment checkout

- Checkout supports Card, UPI, Wallet and NetBanking.
- Each method validates input differently and charges a different fee.
- The buyer picks the method at runtime.
- **Twist:** Split payment — part by wallet, the rest by card — must work without changing the checkout class.

### 1.2 Order routing

- A broker routes each client order to one of several trading venues.
- Routing rules: best price, lowest fee, fastest fill.
- The rule is chosen per client from their profile.
- **Twist:** A "smart" rule tries best price but falls back to fastest fill when the best venue is down. Build it from the existing rules, not by copy-pasting them.

### 1.3 Ride fare pricing

- Fare = base fare + per-km rate + per-minute rate.
- Rates differ for Bike, Auto and Sedan.
- A surge multiplier applies during peak hours.
- **Twist:** A city's pricing rule can be changed while the app runs, with no restart — and rides already in progress keep their original price.

### 1.4 API rate limiter

- An API gateway limits requests per client.
- Support Fixed Window, Sliding Window and Token Bucket.
- Each tier (free, pro, enterprise) uses its own algorithm and limit.
- **Twist:** Every algorithm must be testable without real time passing — no `Thread.sleep` anywhere in the tests.

### 1.5 Search ranking

- An e-commerce search ranks products by relevance, price, rating or newest.
- The user picks the ranking from a dropdown.
- **Twist:** 10% of users get a new experimental ranking for an A/B test. Assignment must be sticky: the same user always sees the same ranking.

## 2. Observer

### 2.1 Stock price alerts

- Users set alerts like "notify me when INFY goes above 1600".
- A price feed pushes updates; matching users are notified.
- Users can remove an alert at any time.
- **Twist:** An alert fires once when the threshold is crossed, not on every tick while the price stays above it.

### 2.2 Market data fan-out

- A feed handler receives FX quotes for EURUSD, GBPUSD, USDJPY and others.
- Consumers: pricing engine, risk engine, UI dashboard, audit log.
- Each consumer receives only the currency pairs it subscribed to.
- **Twist:** The UI is slow. A slow consumer must never delay the pricing engine.

### 2.3 Channel notifications

- Users subscribe to video channels; a new upload notifies every subscriber.
- Each user chooses push, email, or both.
- **Twist:** A user can mute a channel for 24 hours without unsubscribing, and it unmutes itself automatically.

### 2.4 Order status tracking

- An order moves PLACED → PACKED → SHIPPED → DELIVERED.
- Customer, seller and delivery partner are notified on changes.
- Each party cares about a different set of transitions.
- **Twist:** If notifying one party throws, the others are still notified and the status change still succeeds.

### 2.5 Spreadsheet cells

- Cell C1 holds `=A1+B1`. When A1 changes, C1 recomputes automatically.
- Chains work: D1 depends on C1, which depends on A1.
- **Twist:** Reject a circular formula (A1 refers to B1, B1 refers to A1) at the moment it is entered, not when it loops forever.

## 3. Factory Method

### 3.1 Notification factory

- Create EMAIL, SMS, PUSH and WHATSAPP notifications from a type string.
- Client code never calls `new` on a concrete notification class.
- An unknown type fails with a clear error message.
- **Twist:** Adding a new notification type must not require editing the factory's existing code.

### 3.2 Document parser

- Parse PDF, DOCX and CSV files based on file extension.
- Every parser returns a common `Document` with title and text.
- **Twist:** Some files have the wrong extension. Detect the real type from the file's first few bytes, falling back to the extension.

### 3.3 Parking lot vehicles

- Create Car, Bike, Truck and EV objects from a vehicle registration record.
- Each vehicle knows which spot size it needs.
- **Twist:** An EV is a car that also needs a charging spot. Model it without duplicating car logic and without the factory growing an if-else per combination.

### 3.4 Trading instruments

- Create Spot, Forward, Swap and Option instruments from the security-type field of an incoming message.
- Each instrument validates its own required fields.
- **Twist:** Two exchanges send the same instrument in different field formats. Support both without the instrument classes knowing exchanges exist.

## 4. Singleton

### 4.1 Configuration manager

- App configuration is loaded once from a file and readable from anywhere.
- Initialisation is lazy: nothing loads until first use.
- **Twist:** Prove thread safety with a test where 100 threads call `getInstance()` at the same instant and all get the same object, loaded exactly once.

### 4.2 Order ID generator

- Generate unique, strictly increasing order IDs across the whole application.
- Must stay correct under heavy concurrent load.
- **Twist:** It must remain a single instance even through Java serialization and reflection.

### 4.3 Application logger

- One logger shared across the app, writing to the console.
- **Twist:** Each unit test needs a fresh, isolated logger. Make that possible without weakening the single-instance guarantee in production.

## 5. Builder

### 5.1 HTTP request builder

- Build requests with URL, method, headers, query params, body and timeout.
- Only the URL is mandatory; everything else has a sensible default.
- The built request is immutable.
- **Twist:** A GET with a body, or any request without a URL, fails at `build()` with a clear message.

### 5.2 Pizza order

- Choose size, crust, sauce, cheese and any number of toppings.
- Price is computed from the choices.
- **Twist:** Menu presets (Margherita, Farmhouse) start from a fixed recipe but let the customer change any single field.

### 5.3 Trade order builder

- Fields: symbol, side, quantity, order type, limit price, time-in-force.
- LIMIT orders require a price; MARKET orders must not have one.
- **Twist:** Make invalid combinations fail at compile time wherever possible, not just at runtime.

## 6. Decorator

### 6.1 Coffee shop

- Base drinks: Espresso, Latte. Add-ons: milk, sugar, caramel, whipped cream.
- Any number of add-ons in any combination; each adds cost and a description.
- **Twist:** Double caramel charges twice. A rule "no whipped cream on espresso" must be enforced.

### 6.2 Layered file writer

- Write data to a file.
- Optional layers: compression, encryption, buffering, checksum. They stack in any order.
- **Twist:** Reading back must undo the layers in exactly the reverse order, automatically.

### 6.3 Resilient price client

- `PriceClient` fetches prices from a remote service (fake it with random latency and failures).
- Add retry, caching, logging and timing as optional layers.
- **Twist:** Caching must always sit outside retry, never inside. Enforce that ordering in code so a teammate cannot assemble it wrongly.

### 6.4 Text formatting

- Plain text can be made bold, italic, underlined or uppercase, in any combination.
- **Twist:** Remove one formatting layer from the middle of an existing stack at runtime — for example, un-bold text that is also italic and underlined.

## 7. State

### 7.1 Order lifecycle

- States: NEW, PARTIALLY\_FILLED, FILLED, CANCELLED, REJECTED.
- Events: `fill(qty)`, `cancel()`, `amend(newQty)`.
- Each state handles each event differently; an illegal event returns a clear rejection.
- **Twist:** Amending to a quantity below what is already filled must be rejected, and an amend while partially filled keeps the filled quantity intact.

### 7.2 Document workflow

- DRAFT → IN\_REVIEW → APPROVED → PUBLISHED, with REJECTED sending it back to DRAFT.
- Only authors edit drafts; only reviewers approve or reject.
- **Twist:** Add an ARCHIVED state reachable from PUBLISHED without editing any existing state class.

### 7.3 Media player

- States: STOPPED, PLAYING, PAUSED, BUFFERING.
- User buttons: play, pause, stop. Network events: buffering started, buffering finished.
- **Twist:** If the user presses pause during buffering, the player resumes as PAUSED, not PLAYING, once buffering finishes.

### 7.4 ATM session

- States: IDLE, CARD\_INSERTED, PIN\_VERIFIED, OUT\_OF\_CASH.
- Actions: insert card, enter PIN, withdraw, eject.
- Three wrong PINs retain the card.
- **Twist:** Running out of cash mid-session ejects the current card gracefully and blocks new sessions until refilled.

### 7.5 Game character

- States: NORMAL, POWERED\_UP, INVINCIBLE, DEAD.
- Events: take damage, collect power-up, timer expired.
- **Twist:** Power-ups expire after 10 seconds. Test the expiry without any real waiting.

## 8. Command

### 8.1 Text editor undo/redo

- Operations: type text, delete text, apply bold.
- Unlimited undo and redo.
- Any new operation after an undo clears the redo history.
- **Twist:** "Replace all" across 50 matches undoes as a single step.

### 8.2 Smart home remote

- A remote with programmable buttons; any button can be bound to any device action.
- A "party mode" button runs several actions across devices.
- **Twist:** Undoing party mode restores every device to exactly its previous state — including brightness and volume levels, not just on/off.

### 8.3 Background job queue

- Submit jobs (send email, generate report, resize image) now; workers execute them later.
- **Twist:** A failed job retries 3 times with increasing delay, then moves to a dead-letter list. Jobs must survive an application restart.

### 8.4 Order gateway actions

- Actions: NewOrder, CancelOrder, AmendOrder, sent to a fake exchange gateway.
- Every action is logged before it executes.
- **Twist:** After a crash, replay the log to rebuild exact state — without re-sending any order to the exchange.

### 8.5 Drawing macro recorder

- Shape operations: draw, move, resize, delete.
- Record a sequence as a macro and replay it at a different position on the canvas.
- **Twist:** A macro can contain other macros, to any depth.

## 9. Chain of Responsibility

### 9.1 Support ticket escalation

- Levels: bot → L1 agent → L2 engineer → manager.
- Each level resolves what it can and passes the rest on.
- **Twist:** VIP customers skip the bot. The chain differs per customer without creating new handler classes.

### 9.2 Pre-trade risk checks

- An order passes: format validation → credit limit → position limit → fat-finger price check.
- Any check can reject with a reason; the order reaches the exchange only if all pass.
- **Twist:** In dry-run mode, report every failure at once. In live mode, stop at the first failure. Same handler classes for both.

### 9.3 HTTP middleware

- A request passes: authentication → rate limiting → logging → handler.
- Any step can short-circuit and return a response early.
- **Twist:** Logging must record the final response status, even when a step after it short-circuits.

### 9.4 Expense approval

- Up to 1,000: team lead. Up to 10,000: manager. Up to 100,000: director. Above: CFO.
- **Twist:** An approver on leave has a delegate configured at runtime; their requests route to the delegate automatically.

## 10. Adapter

### 10.1 Payment gateway integration

- Your app uses `PaymentProcessor.pay(amountInPaise, currency)`.
- Integrate two fake SDKs with different method names, money units (paise vs decimal rupees) and exception types.
- **Twist:** One SDK is asynchronous with callbacks; your interface is synchronous.

### 10.2 Legacy inventory service

- An old inventory service returns XML strings; new code expects `Product` objects.
- The legacy service cannot be changed.
- **Twist:** The legacy service reports price in the wrong unit for one category. Fix it in the adapter, and only there.

### 10.3 Market data vendors

- Internal interface: `Quote getQuote(String pair)`.
- Vendor A sends "EUR/USD" with bid and ask as strings. Vendor B sends "EURUSD" as a mid price plus spread.
- **Twist:** The vendor is chosen by config, and callers never learn which vendor answered.

## 11. Facade

### 11.1 Home theatre

- Subsystems: projector, amplifier, streaming player, lights, screen.
- Two calls only: `watchMovie(title)` and `endMovie()`.
- **Twist:** If the projector fails to start, everything already switched on shuts down cleanly in reverse order.

### 11.2 Order placement

- Placing an order touches inventory, payment, shipping and notification services.
- The client calls a single method.
- **Twist:** If shipping fails after payment succeeded, the customer is refunded and the reserved stock released.

### 11.3 Video conversion library

- Wrap a complex conversion library (codec, bitrate, audio mixer — all fakes) behind `convert(file, format)`.
- **Twist:** Power users can pass advanced options without the simple method signature growing at all.

## 12. Composite

### 12.1 File system size

- Files and folders; folders contain files and other folders.
- `size()` works on any node.
- **Twist:** Add symbolic links without double-counting size and without looping forever on a cycle.

### 12.2 Organisation chart

- Employees and managers; managers have direct reports.
- Compute the total salary cost of any subtree, and print the tree indented.
- **Twist:** Find the lowest common manager of any two employees.

### 12.3 Portfolio of portfolios

- A portfolio holds positions and sub-portfolios, to any depth.
- Compute market value and P&L at any level.
- **Twist:** If the same position is linked into two sub-portfolios, the parent counts it once.

### 12.4 UI component tree

- Panels contain buttons, text boxes and other panels.
- `render()` and `setEnabled()` work on any component.
- **Twist:** Disabling a panel disables its children; re-enabling restores each child's own previous state, not blanket "enabled".

## 13. Proxy

### 13.1 Lazy image gallery

- A gallery holds 1,000 high-resolution images.
- An image loads from disk only when first displayed.
- **Twist:** If two threads request the same unloaded image at the same moment, it loads exactly once.

### 13.2 Document access control

- A document service offers read, edit and delete.
- Viewers read; editors read and edit; admins do everything.
- **Twist:** Every denied attempt is audited, without modifying the real document service.

### 13.3 Exchange rate cache

- `ExchangeRateService.getRate(from, to)` is slow (fake 500 ms).
- Cache each result for 60 seconds.
- **Twist:** Test expiry without waiting 60 seconds, and make a burst of concurrent calls for the same uncached pair trigger only one real call.

### 13.4 Rate-limited third-party API

- Wrap an external API that allows at most 10 calls per second.
- Callers must never exceed the limit.
- **Twist:** Excess calls wait up to 2 seconds for a slot, then fail fast with a clear error.

## 14. Template Method

### 14.1 Data import pipeline

- Import from CSV, JSON and XML: read → parse → validate → save.
- Read and parse differ per format; validate and save are shared.
- **Twist:** Some importers need an extra "enrich" step between validate and save, without forcing every importer to implement it.

### 14.2 Report generation

- Every report is header → body → footer, in PDF and HTML flavours.
- **Twist:** Subclasses must be unable to reorder or skip steps.

### 14.3 Game AI turn

- Every turn: gather resources → build → attack.
- Aggressive and defensive AIs differ in how they build and attack.
- **Twist:** Build the same thing a second time using Strategy instead. Write three lines on which version you would keep, and why.

## 15. Abstract Factory

### 15.1 Cross-platform UI kit

- Widgets: Button, Checkbox, TextField, each in a Windows and a Mac style.
- Client code builds a form without knowing the platform.
- **Twist:** Mixing a Windows button with a Mac checkbox in one form must be impossible.

### 15.2 Database access families

- Connection, Command and Transaction objects for MySQL and PostgreSQL (fakes).
- **Twist:** Add a document database where "transaction" means something different. Decide what your abstraction promises, and write down why.

### 15.3 Exchange connectivity

- Each exchange needs a matching OrderEncoder, MarketDataDecoder and SessionHandler.
- Support two exchanges.
- **Twist:** The exchange is chosen at runtime from config, and adding a third touches no existing code.

## 16. Iterator

### 16.1 Music playlist

- Iterate a playlist in order, shuffled, or on repeat.
- **Twist:** Modifying the playlist during iteration fails fast, the way Java's own collections do.

### 16.2 Paginated API

- Iterate over all users from an API that returns 50 per page (fake it).
- Callers use a plain for-each loop.
- **Twist:** The next page is fetched lazily, only when the caller reaches the end of the current one.

### 16.3 Binary tree traversal

- Iterate a binary tree in-order, pre-order and level-order.
- **Twist:** No recursion, and an iteration can be paused mid-traversal and resumed later.

## 17. Null Object

### 17.1 Optional discount

- A customer may or may not have a discount.
- Remove every scattered `if (discount != null)` check.
- **Twist:** Analytics must still distinguish "no discount" from "a 0% discount".

### 17.2 Guest user

- `findUser(id)` returns a guest user instead of `null` for unknown IDs.
- A guest can browse but cannot check out.
- **Twist:** A guest must never be saved to the database by accident.

## 18. Dependency Injection

### 18.1 Manual wiring

- `OrderService` needs a repository, a payment gateway and a clock.
- Wire the whole object graph by hand in `main` — no framework.
- **Twist:** Write a test that runs `OrderService` with every dependency faked, including time.

### 18.2 Mini container

- Register interface → implementation mappings; resolve a full object graph automatically.
- **Twist:** Detect a circular dependency at registration time and report the full cycle in the error message.

### 18.3 Environment switching

- The same app runs with real or fake email and payment, based on an environment flag.
- **Twist:** Not a single `if (env == TEST)` anywhere in business code.

## 19. Repository

### 19.1 User store

- Create, read, update and delete users behind an interface, with an in-memory implementation.
- **Twist:** Add a file-backed implementation. Business code and existing tests stay unchanged.

### 19.2 Trade store

- Store and query trades by account, symbol and date range.
- **Twist:** Callers combine any filters (account AND symbol AND date range) without a dedicated method per combination, and no storage detail leaks into the service layer.

## 20. Producer-Consumer

### 20.1 Bounded buffer

- Build your own bounded blocking queue — no `java.util.concurrent` queues allowed.
- Producers block when full; consumers block when empty.
- **Twist:** Support a timeout on both put and take.

### 20.2 Market data conflation

- One producer pushes up to 1 million price ticks per second; several consumers process them.
- **Twist:** When consumers fall behind, only the latest price per symbol matters. Drop stale ticks, never the latest one.

### 20.3 Batched log shipping

- Application threads produce log lines; one background thread writes them in batches.
- A batch flushes at 100 lines or after 1 second, whichever comes first.
- **Twist:** On shutdown, every pending line is flushed. Nothing is lost.

### 20.4 Per-account ordering

- Orders from many accounts are processed by a pool of worker threads.
- **Twist:** Orders for the same account are processed strictly in arrival order; different accounts run in parallel.

## 21. Object Pool

### 21.1 Connection pool

- A pool of N fake database connections; callers borrow and return them.
- When none are free, callers wait up to a timeout.
- **Twist:** A broken connection is detected on borrow and replaced transparently.

### 21.2 Message object reuse

- Reuse order-message objects on a hot path to avoid garbage-collection pressure.
- **Twist:** Prove with a test that no field value from a previous borrower ever leaks to the next one.

### 21.3 Generic pool with leak detection

- A generic `Pool<T>` with minimum and maximum size that grows on demand.
- **Twist:** If an object is not returned within 30 seconds, log a warning that identifies where it was borrowed from.

## 22. Bridge

### 22.1 Shapes and renderers

- Shapes: Circle, Square. Renderers: Vector, Raster.
- **Twist:** Adding a Triangle, or an SVG renderer, means writing exactly one new class — never one per combination.

### 22.2 Messages and channels

- Message types: Alert, Reminder, Promotion. Channels: Email, SMS, Push.
- **Twist:** Promotions must never go out by SMS. Enforce this without adding a class per combination.

### 22.3 Remotes and devices

- Remotes: Basic, and Advanced (adds mute and favourites). Devices: TV, Radio.
- **Twist:** Add a Projector; every existing remote works with it immediately, unchanged.

## 23. Prototype

### 23.1 Game enemy spawner

- Spawn many enemies (Goblin, Orc) by cloning configured templates.
- **Twist:** Every clone owns its own inventory list. Changing one clone's inventory never affects another clone or the template.

### 23.2 Contract templates

- Create contracts from template documents with sections, styles and attachments.
- **Twist:** Attachments are huge and never change, so clones share them; sections are copied so each contract can be edited independently.

## 24. Mediator

### 24.1 Chat room

- Users send messages to a room, which delivers them to everyone else.
- Users never hold references to each other.
- **Twist:** Add private messages and per-user mute lists, still without users knowing about each other.

### 24.2 Air traffic control

- Planes request landing and take-off slots through a control tower.
- **Twist:** An emergency landing pre-empts the queue, and every plane in the holding pattern is re-sequenced.

### 24.3 Checkout form

- "Same as billing" disables the shipping fields; choosing a country reloads the state list; Submit is enabled only when the form is valid.
- **Twist:** Add a gift-wrap option that interacts with two existing fields, without editing any existing field class.

## 25. Memento

### 25.1 Editor snapshots

- Save and restore editor state: text, cursor position and selection.
- **Twist:** The object storing snapshots can neither read nor modify what is inside them.

### 25.2 Game checkpoints

- Save checkpoints and load any earlier one.
- **Twist:** Keep only the last 5, and make each save cheap by storing only what changed since the previous checkpoint.

### 25.3 Order book snapshot

- Snapshot the order book at end of day and restore it the next morning.
- **Twist:** Snapshots are taken while trading continues. The snapshot must be internally consistent, and the book must never pause for long.

## 26. Flyweight

### 26.1 Styled characters

- A document of 1 million characters, each with font, size and colour.
- **Twist:** Memory grows with the number of distinct styles, not the number of characters. Measure it and report the numbers.

### 26.2 Forest rendering

- Render 1 million trees; each has a type (texture, mesh) and a position.
- **Twist:** Add per-tree health that changes over time, without breaking the sharing.

### 26.3 Instrument reference data

- 500,000 orders reference currency pairs and their static data (tick size, decimal places).
- **Twist:** Tick size changes intraday. Existing orders keep the old value, new orders get the new one, and static data is never copied per order.

## 27. Visitor

### 27.1 Shopping cart tax

- Items: Book, Electronics, Food, each taxed by different rules.
- **Twist:** Add a shipping-cost calculation without modifying any item class.

### 27.2 Expression tree

- Nodes: Number, Add, Multiply. Operations: evaluate, print, simplify.
- **Twist:** Add a Subtract node. Note everything that had to change, then write three lines on whether Visitor was still the right call.

### 27.3 Portfolio risk

- Instruments: Stock, Bond, Option. Operations: market value, delta, margin requirement.
- **Twist:** Risk adds a new operation every week. Adding one never touches instrument classes.

## 28. Interpreter

### 28.1 Boolean rule engine

- Parse and evaluate rules like `age > 18 AND (country == "IN" OR vip == true)`.
- **Twist:** When a rule evaluates false, report exactly which sub-expression made it false.

### 28.2 Trading alert language

- Rules like `EURUSD > 1.10 AND VOLUME(EURUSD) > 1000000`, evaluated against live quotes.
- **Twist:** Unknown symbols or functions are rejected when the rule is created, never discovered during evaluation.

## 29. Mixed Round

No pattern is named. Before coding, write one line per problem: which pattern you chose and why.

### 29.1 Coupon engine

- Coupon conditions: minimum cart value, product category, first order, payment method.
- Conditions combine with AND, OR and NOT, nested to any depth.
- Marketing adds new condition types every month.
- **Twist:** When a coupon is rejected, tell the customer exactly which condition failed.

### 29.2 Logging framework

- Levels: DEBUG, INFO, WARN, ERROR.
- Outputs: console, file and remote, each with its own minimum level.
- Each output formats messages as plain text or JSON.
- **Twist:** Logging never slows down or crashes the calling thread, even while the remote output is down.

### 29.3 Food delivery order

- An order moves placed → accepted → cooking → picked up → delivered, or cancelled.
- Customer, restaurant and rider are each notified of the changes they care about.
- **Twist:** Refund on cancellation depends on the stage: full before cooking, 50% during cooking, nothing after pickup.

### 29.4 Spreadsheet with undo

- Edit cells; formulas recompute when their inputs change.
- **Twist:** Undo any edit in one step, including one that triggered a cascade of recomputations.

### 29.5 Notification preferences

- Each user picks channels per notification type, sets quiet hours, and can opt into a daily digest.
- **Twist:** Adding a new channel touches no existing class.

### 29.6 File upload pipeline

- Uploads pass: virus scan → resize → watermark → store.
- Which steps run depends on the file type.
- **Twist:** If a step fails, a retry resumes from that step. Earlier successful steps never re-run.

### 29.7 Multi-exchange gateway

- One order API in front of three exchanges with different protocols and message formats.
- Each exchange enforces its own rate limit.
- **Twist:** Moving an account to another exchange is a config change, and no exchange's rate limit can ever be exceeded.

### 29.8 Parking fee calculator

- Rules: hourly rate, daily cap, weekend rate, EV discount, lost-ticket penalty.
- **Twist:** Rules combine — an EV, parked over a weekend, with a lost ticket — and the order they apply in changes the total. Make the order explicit and testable.
