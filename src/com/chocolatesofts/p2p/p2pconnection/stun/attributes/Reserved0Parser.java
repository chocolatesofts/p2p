package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved0;

import java.io.DataOutputStream;

public class Reserved0Parser implements AttributeRegistry.Serializer<Reserved0> {
    public static char TYPE = 0x0000;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved0 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved0 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
