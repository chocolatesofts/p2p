package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import com.chocolatesofts.p2p.p2pconnection.stun.models.XORMappedAddress;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XORMappedAddressParser implements AttributeRegistry.Serializer<XORMappedAddress> {
    public static char TYPE = 0x0020;
    private static final XORMappedAddressParser instance = new XORMappedAddressParser();
    private XORMappedAddressParser(){}
    @Override
    public char getUniqueType() {
        return TYPE;
    }

    @Override
    public XORMappedAddress deserialize(final TLV tlv, final StunHeader header) {
        try {
            final AddressHolder addressHolder = AddressParser.parse(tlv, header);

            char port = addressHolder.getPort();
            port ^= (StunHeader.MAGIC_COOKIE>>16);

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final DataOutputStream daos = new DataOutputStream(baos);
            header.serializeTransacrionId(daos);
            daos.close();
            final byte[] transactionIdBytes = baos.toByteArray();
            byte[] address = addressHolder.getAddress();
            for(int i=0; i<address.length; i++) {
                address[i] ^= transactionIdBytes[i];
            }

            return new XORMappedAddress(port, address);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnknownError("Unexpected IO/ERROR while parsing XORMappedAddress attribute");
        }
    }

    @Override
    public void serialize(final XORMappedAddress attribute, final DataOutputStream outputStream) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static XORMappedAddressParser getInstance() {
        return instance;
    }
}
