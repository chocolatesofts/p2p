package com.chocolatesofts.p2p.p2pconnection.stun.protocol;

import java.util.HashMap;
import java.util.Set;

public class StunMessage {
    private final StunHeader stunHeader;
    private final HashMap<Class<?>, Object> attributes;
    public StunMessage(StunHeader stunHeader, HashMap<Class<?>, Object> attributes) {
        this.stunHeader = stunHeader;
        this.attributes = attributes;
    }

    public <T> T getAttribute(final Class<T> tClass) {
        if(hasAttribute(tClass))
            return (T)attributes.get(tClass);
        throw new IllegalArgumentException(String.format("No attribute of class %s found", tClass.getName()));
    }

    public <T> boolean hasAttribute(final Class<T> tClass) {
        return attributes.containsKey(tClass);
    }

    public void addAttribute(Object attribute) {
        attributes.put(attribute.getClass(), attribute);
    }

    public StunHeader getHeader() {
        return stunHeader;
    }

    public Set<Class<?>> getAttributeSet() {
        return attributes.keySet();
    }

    public boolean sameTransaction(final StunMessage stunMessage) {
        if(stunMessage == null)
            return false;
        return getHeader().sameTransaction(stunMessage.getHeader());
    }
}
