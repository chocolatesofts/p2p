package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved4;

import java.io.DataOutputStream;

public class Reserved4Parser implements AttributeRegistry.Serializer<Reserved4> {
    public static char TYPE = 0x0004;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved4 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved4 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
