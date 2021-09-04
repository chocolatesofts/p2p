package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import com.chocolatesofts.p2p.p2pconnection.util.PrimitiveTypesUtil;
import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunHeader;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import java.io.IOException;

public class AddressParser {
    public static AddressHolder parse(final TLV tlv, final StunHeader header) throws IOException {
        try {
            final char addressFamily = PrimitiveTypesUtil.asCharacter(tlv.getValue(), 0);
            if((addressFamily & 0xFF) != 1 && (addressFamily & 0xFF) != 2)
                throw new IllegalArgumentException("Invalid address family");

            final byte[] address = new byte[((addressFamily & 0xFF) == 1)? 4 : 16];
            char port = PrimitiveTypesUtil.asCharacter(tlv.getValue(), 2);
            System.arraycopy(tlv.getValue(), 4, address, 0, address.length);
            return new AddressHolder(port, address);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Unexpected address format");
        }
    }
}
