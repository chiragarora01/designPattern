package org.practice.factory.factory4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MessageFactory {
    private final Map<SecurityType, Function<Message, Msg>> msgMap = new ConcurrentHashMap<>();

    public MessageFactory() {
        msgMap.put(SecurityType.SPT, spotMessage::new);
        msgMap.put(SecurityType.FWD, fwdMsg::new);
        msgMap.put(SecurityType.SWP, swpMsg::new);
        msgMap.put(SecurityType.OPTIONS, optionMsg::new);
    }

    public Msg getMsgType(Message message) {
        SecurityType securityType = message.getSecurityType();
        // field validation lives in each instrument's constructor, not here
        Function<Message, Msg> messageMsgFunction = msgMap.get(securityType);
        if (messageMsgFunction == null) {
            throw new IllegalArgumentException("Unknown security type: " + securityType);
        }
        return messageMsgFunction.apply(message);
    }
}