package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import com.chocolatesofts.p2p.p2pconnection.stun.models.MappedAddress;

import java.io.DataOutputStream;
import java.io.IOException;

public class MappedAddressParser implements AttributeRegistry.Serializer<MappedAddress> {

    public static char TYPE = 0x0001;
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    private static final MappedAddressParser instance = new MappedAddressParser();
    private MappedAddressParser(){}
    public static MappedAddressParser getInstance() {
        return instance;
    }

    @Override
    public MappedAddress deserialize(TLV tlv, StunHeader header) {
        try {
            final AddressHolder addressHolder = AddressParser.parse(tlv, header);
            return new MappedAddress(addressHolder.getPort(), addressHolder.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnknownError("Unexpected IO/ERROR while parsing MappedAddress attribute");
        }
    }

    @Override
    public void serialize(MappedAddress attribute, DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
