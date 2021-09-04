package com.chocolatesofts.p2p.p2pconnection.stun.models;

import com.chocolatesofts.p2p.p2pconnection.util.PrimitiveTypesUtil;

public class AddressHolder {
    private final char port;
    private final byte[] address;
    public AddressHolder(final char port, final byte[] address) {
        if(address.length != 16 && address.length != 4)
            throw new UnsupportedOperationException("Invalid address family");
        this.port = port;
        this.address = address;
    }

    public char getPort(){
        return this.port;
    }

    public byte[] getAddress() {
        byte[] returnAddress = new byte[address.length];
        System.arraycopy(address, 0 , returnAddress, 0, returnAddress.length);
        return returnAddress;
    }

    public String getAddressAsString() {
        final StringBuilder addressStr;
        if(address.length == 4) {
            addressStr = new StringBuilder();
            for(int i=0; i<4; i++) {
                addressStr.append((int) PrimitiveTypesUtil.unsignedByte(address[i]));
                if(i<3)
                    addressStr.append('.');
            }
        } else {
            addressStr = new StringBuilder("[");
            for(int i=0; i<16; i++) {
                addressStr.append(PrimitiveTypesUtil.asHexChar(address[i]));
                if(i%2 == 1 && i < 15)
                    addressStr.append(':');
            }
            addressStr.append("]");
        }
        return addressStr.toString();
    }

    @Override
    public String toString() {
        return getAddressAsString() + ":" + (int)port;
    }
}
