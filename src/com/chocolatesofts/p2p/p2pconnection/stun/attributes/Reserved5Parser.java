package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved5;

import java.io.DataOutputStream;

public class Reserved5Parser implements AttributeRegistry.Serializer<Reserved5> {
    public static char TYPE = 0x0005;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved5 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved5 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
