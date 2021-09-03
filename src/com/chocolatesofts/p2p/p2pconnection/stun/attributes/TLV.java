package com.chocolatesofts.p2p.p2pconnection.stun.attributes;

import java.io.IOException;
import java.io.InputStream;

public class TLV {

    private final char type, length;
    private final byte[] value;

    TLV(final char type, final char  length, final byte[] value) {
        this.type = type;
        this.length = length;
        this.value = value;
    }

    public static TLV parse(final InputStream inputStream) throws IOException, IllegalArgumentException {
        char type = 0, len = 0;
        byte[] typeLenRead = inputStream.readNBytes(4);
        if(typeLenRead.length != 4)
            throw new IllegalArgumentException("Not enough bytes");

        len += typeLenRead[3];
        len +=typeLenRead[2]<<8;
        if(len%4 != 0) {
            len >>= 2;
            len <<= 2;
            len += 4;
        }

        type+=typeLenRead[1];
        type+=typeLenRead[0]<<8;

        byte[] valueRead = inputStream.readNBytes(len);
        if(valueRead.length != len) {
            throw new IllegalArgumentException("Not enough bytes");
        }
        return new TLV(type, len, valueRead);
    }

    public char getType() {
        return type;
    }

    public byte[] getValue() {
        return value;
    }
}
