package com.chocolatesofts.p2p.p2pconnection.stun.methods;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunMessage;
import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunSerializer;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import com.chocolatesofts.p2p.p2pconnection.stun.models.MappedAddress;
import com.chocolatesofts.p2p.p2pconnection.stun.models.XORMappedAddress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class BindingMethod {
    public static final char METHOD = 0x0001;
    public static final HashSet<Class<?>> attributes = new HashSet<>();
    static {
        attributes.add(XORMappedAddress.class);
        attributes.add(MappedAddress.class);
    }

    public static StunMessage newRequestPayload(final OutputStream outputStream) throws IOException {
        final StunHeader stunHeader = new StunHeader();
        stunHeader.randomizeTransactionId();
        stunHeader.setMessageMethod(METHOD);
        stunHeader.setMessageClass(StunHeader.MessageClass.REQUEST);

        final StunMessage stunMessage = new StunMessage(stunHeader, new HashMap<>());
        byte[] stunMessageBytes = StunSerializer.serialize(stunMessage);
        outputStream.write(stunMessageBytes);
        return stunMessage;
    }

    public static boolean hasMappedAddress(final StunMessage stunMessage) {
        return stunMessage != null && (stunMessage.hasAttribute(XORMappedAddress.class) || stunMessage.hasAttribute(MappedAddress.class));
    }

    public static StunMessage deserializeResponsePayload(final InputStream inputStream) throws IOException {
        StunMessage stunMessage = StunSerializer.deserializeSuccess(inputStream, attributes, METHOD);
        if (!hasMappedAddress(stunMessage)) {
            throw new UnknownError("Internal error occurred");
        }
        return stunMessage;
    }

    public static AddressHolder getAddress(final StunMessage stunMessage) {
        if (!hasMappedAddress(stunMessage)) {
            throw new IllegalArgumentException("Message does not have any mapped address");
        }
        if (stunMessage.hasAttribute(XORMappedAddress.class))
            return stunMessage.getAttribute(XORMappedAddress.class);
        return stunMessage.getAttribute(MappedAddress.class);
    }
}
