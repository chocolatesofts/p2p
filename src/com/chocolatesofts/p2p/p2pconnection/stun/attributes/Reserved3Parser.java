package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved3;

import java.io.DataOutputStream;

public class Reserved3Parser implements AttributeRegistry.Serializer<Reserved3> {
    public static char TYPE = 0x0003;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved3 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved3 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
