package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved2;

import java.io.DataOutputStream;

public class Reserved2Parser implements AttributeRegistry.Serializer<Reserved2> {
    public static char TYPE = 0x0002;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved2 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved2 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
