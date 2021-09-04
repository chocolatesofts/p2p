package com.chocolatesofts.p2p.p2pconnection.holepunch;

import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import com.chocolatesofts.p2p.p2pconnection.util.PrimitiveTypesUtil;

public class AddressEncodeDecoder {
    public static String encode(final AddressHolder addressHolder) {
        return PrimitiveTypesUtil.asHexString(addressHolder.getAddress()) + PrimitiveTypesUtil.asHexString(addressHolder.getPort());
    }

    public static AddressHolder decode(final String encoded) {
        final String address = encoded.substring(0, encoded.length()-4);
        final String port = encoded.substring(encoded.length()-4);
        return new AddressHolder(PrimitiveTypesUtil.asChar(port), PrimitiveTypesUtil.asBytes(address));
    }
}
