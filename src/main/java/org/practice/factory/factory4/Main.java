package org.practice.factory.factory4;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        MessageFactory factory = new MessageFactory();

        // same spot trade from two exchanges, different wire formats -> both become spotMessage
        Msg fromA = factory.getMsgType(new ExchangeAAdapter(1234.50, 5_000, SecurityType.SPT).translate());
        Msg fromB = factory.getMsgType(new ExchangeBAdapter("1,234.50", "500", SecurityType.SPT).translate());
        check("spot from A", fromA instanceof spotMessage);
        check("spot from B", fromB instanceof spotMessage);

        Msg fwd = factory.getMsgType(new Message.Builder().securityType(SecurityType.FWD)
                .price(1.1).amount(2.0).fwdPts(12.5).fwdDate(LocalDate.now()).build());
        check("forward", fwd instanceof fwdMsg);

        Msg swp = factory.getMsgType(new Message.Builder().securityType(SecurityType.SWP)
                .price(1.1).amount(2.0).fwdPts(12.5).fwdDate(LocalDate.now())
                .farPts(13.5).farDate(LocalDate.now().plusDays(1)).build());
        check("swap", swp instanceof swpMsg);

        Msg opt = factory.getMsgType(new Message.Builder().securityType(SecurityType.OPTIONS)
                .price(1.1).amount(2.0).expiryDate(LocalDate.now().plusDays(7)).build());
        check("option", opt instanceof optionMsg);

        // each instrument validates its own required fields
        expectFailure("spot rejects zero price", () ->
                factory.getMsgType(new Message.Builder().securityType(SecurityType.SPT).amount(10).build()));
        expectFailure("forward rejects missing date", () ->
                factory.getMsgType(new Message.Builder().securityType(SecurityType.FWD)
                        .price(1.1).amount(2.0).fwdPts(1).build()));
        expectFailure("option rejects missing expiry", () ->
                factory.getMsgType(new Message.Builder().securityType(SecurityType.OPTIONS)
                        .price(1.1).amount(2.0).build()));

        System.out.println("all checks passed");
    }

    private static void check(String name, boolean ok) {
        if (!ok) {
            throw new AssertionError(name);
        }
        System.out.println("ok: " + name);
    }

    private static void expectFailure(String name, Runnable action) {
        try {
            action.run();
            throw new AssertionError(name + ": expected failure");
        } catch (IllegalArgumentException e) {
            System.out.println("ok: " + name);
        }
    }
}
