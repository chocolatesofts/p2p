package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.ReservedB;

import java.io.DataOutputStream;

public class ReservedBParser implements AttributeRegistry.Serializer<ReservedB> {
    public static char TYPE = 0x000B;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public ReservedB deserialize(TLV tlv, StunHeader header) {
        return null;
    }

    @Override
    public void serialize(ReservedB attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
