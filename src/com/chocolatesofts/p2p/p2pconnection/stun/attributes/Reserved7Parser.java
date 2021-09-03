package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.Reserved7;

import java.io.DataOutputStream;

public class Reserved7Parser implements AttributeRegistry.Serializer<Reserved7> {
    public static char TYPE = 0x0007;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public Reserved7 deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(Reserved7 attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
