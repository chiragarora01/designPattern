package org.practice.strategy.strategy3;


import org.practice.strategy.strategy3.strategy.*;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        RideFairStrategy rideFairStrategy = new AutoFairStrategy();
        RideFairStrategy bikeFairStrategy = new BikeFairStrategy();
        RideFairStrategy sedanFairStrategy = new SedanFairStrategy();

        User user = new User("1", "name", VehicleType.Auto, rideFairStrategy);
        User user2 = new User("2", "name", VehicleType.BIKE, bikeFairStrategy);
        User user3 = new User("3", "name", VehicleType.SEDAN, sedanFairStrategy);
        RideFairStrategy surge = new SurgeFairStrategy(sedanFairStrategy,50);
        User user4 = new User("4", "name", VehicleType.SEDAN, surge);

        Invoice pay = user.getRideFairStrategy().pay(500, Duration.ofHours(10));
        Invoice pay1 = user2.getRideFairStrategy().pay(400, Duration.ofHours(9));
        Invoice pay2 = user3.getRideFairStrategy().pay(300, Duration.ofHours(8));
        Invoice pay3 = user4.getRideFairStrategy().pay(200, Duration.ofHours(5));

        System.out.println(pay.toString());
        System.out.println(pay1.toString());
        System.out.println(pay2.toString());
        System.out.println(pay3.toString());

    }
}
