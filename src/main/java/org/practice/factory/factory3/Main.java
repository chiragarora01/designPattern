package org.practice.factory.factory3;

public class Main {
    public static void main(String[] args) {
        ParkingFactory factory = new ParkingFactory();

        Vehicle car = factory.getVehicle(new VehicleDetails("KA-01", "petrol", VehicleType.CAR, false));
        check("car", car.getParkingSize() == ParkingSize.MEDIUM && !car.needsCharging());

        // EV is a Car (no duplicated logic) that also needs charging
        Vehicle ev = factory.getVehicle(new VehicleDetails("KA-02", "electric", VehicleType.CAR, true));
        check("ev", ev instanceof Car && ev.getParkingSize() == ParkingSize.MEDIUM && ev.needsCharging());

        Vehicle bike = factory.getVehicle(new VehicleDetails("KA-03", null, VehicleType.BIKE, false));
        check("bike", bike.getParkingSize() == ParkingSize.SMALL);

        Vehicle truck = factory.getVehicle(new VehicleDetails("KA-04", "diesel", VehicleType.TRUCK, false));
        check("truck", truck.getParkingSize() == ParkingSize.LARGE);

        try {
            factory.getVehicle(new VehicleDetails("KA-05", null, null, false));
            throw new AssertionError("expected failure");
        } catch (IllegalArgumentException e) {
            check("unknown type", true);
        }

        System.out.println("all checks passed");
    }

    private static void check(String name, boolean ok) {
        if (!ok) {
            throw new AssertionError(name);
        }
        System.out.println("ok: " + name);
    }
}
