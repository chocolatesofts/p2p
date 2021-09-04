package com.chocolatesofts.p2p.p2pconnection.util;

public class PrimitiveTypesUtil {

    public static char unsignedByte(final byte b) {
        return (char)((b) & 0xFF);
    }

    public static char asHexChar(final byte b) {
        if(b >= 16)
            throw new IllegalArgumentException("Byte cannot be converted to hex char");
        if(b<10)
            return (char)(48 + b);
        return (char)('a' + b -10);
    }

    public static byte asByte(char c) {
        c = Character.toLowerCase(c);
        if ((c >= '0' && c <= '9'))
            return (byte)(c-'0');
        if (c >= 'a' && c <= 'f')
            return (byte)(c-'a'+10);
        throw new IllegalArgumentException("Invalid hex character");
    }

    public static long fromHexStr(final String hexStr, final int maxLen) {
        if(hexStr.length() > maxLen || hexStr.length() < 1) {
            throw new IllegalArgumentException("Invalid hex string length");
        }
        long ret = 0;
        for (int i=0; i<hexStr.length(); i++)
        {
            ret = (ret<<4) + asByte(hexStr.charAt(i));
        }
        return ret;
    }

    public static byte asByte(final String hexStr) {
        return (byte)(fromHexStr(hexStr, 2));
    }
    public static char asChar(final String hexStr) {
        return (char)(fromHexStr(hexStr, 4));
    }

    public static String asHexString(final byte[] bytes) {
        final StringBuilder hexStr = new StringBuilder();
        for (byte b : bytes) {
            final char uByte = unsignedByte(b);
            hexStr.append(asHexChar((byte) (uByte >> 4)));
            hexStr.append(asHexChar((byte) (uByte & 0x0F)));
        }
        return hexStr.toString();
    }

    public static byte[] asBytes(final String hexStr) {
        if (hexStr.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string");
        }
        byte[] bytes = new byte[hexStr.length()/2];
        for (int i=0; i < bytes.length; i++) {
            bytes[i] = asByte(hexStr.substring(2*i, 2*(i+1)));
        }
        return bytes;
    }

    public static String asHexString(final char c) {
        return asHexString(new byte[]{(byte)(c >> 8), (byte)(c & 0xFF)});
    }

    public static long unsignedInteger(final int n) {
        return ((1L<<32) + n) & 0xFFFFFFFFL;
    }

    public static char asCharacter(final byte[] bytes, final int i) {
        return (char)((bytes[i]<<8) + unsignedByte(bytes[i+1]));
    }

    public static int asInteger(final byte[] bytes, final int i) {
        return (asCharacter(bytes, i)<< 16) + asCharacter(bytes, i+2);
    }

    public static long asUnsignedInteger(final byte[] bytes, final int i) {
        return unsignedInteger(asInteger(bytes, i));
    }

    public static long asLong(final byte[] bytes, final int i) {
        return (asUnsignedInteger(bytes, i)<<32) + asUnsignedInteger(bytes, i+4);
    }
}
