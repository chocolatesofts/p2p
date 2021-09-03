package com.chocolatesofts.p2p.p2pconnection.stun.protocol;

import com.chocolatesofts.p2p.p2pconnection.stun.attributes.AttributeRegistry;
import com.chocolatesofts.p2p.p2pconnection.stun.attributes.TLV;
import com.chocolatesofts.p2p.p2pconnection.stun.util.PrimitiveTypesUtil;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class StunSerializer {

    private boolean hasAttribute(final HashSet<Class<?>> attributes, final Class<?> clazz) {
        if(attributes==null)
            return true;
        return attributes.contains(clazz);
    }

    public static byte[] serialize(final StunMessage stunMessage)
            throws IOException {
        final StunHeader stunHeader = stunMessage.getHeader();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream daos = new DataOutputStream(baos);
        stunHeader.serializeMessageType(daos);
        daos.writeChar(0);
        stunHeader.serializeTransacrionId(daos);

        final var attributeSet = stunMessage.getAttributeSet();
        for(var attribute : attributeSet) {
            AttributeRegistry.Serializer serialzier = AttributeRegistry.getAttributeSerializer(attribute);
            var attributeValue = stunMessage.getAttribute(attribute);
            serialzier.serialize(attributeValue, daos);
        }
        daos.flush();
        byte[] message = baos.toByteArray();
        daos.close();
        baos.close();
        final char messageLength = (char)(message.length - 20);
        message[2] = (byte)(messageLength >> 8);
        message[3] = (byte)(messageLength & 0xFF);
        return message;
    }




    public static StunMessage deserializeSuccess(final InputStream inputStream, final HashSet<Class<?>> attributes,
                                    final char method) throws IOException {
        final StunHeader stunHeader = new StunHeader();

        final byte[] header = inputStream.readNBytes(20);
        if (header.length != 20)
            throw new IllegalStateException("Not enough bytes");
        if ((header[0] & 0x01) != 0x01 || (header[1] & 0x10) != 0)
            return null;
        header[0] &= ~(0x01);
        header[1] &= ~(0x10);
        if (method != PrimitiveTypesUtil.asCharacter(header, 0)) {
            return null;
        }

        final char len = PrimitiveTypesUtil.asCharacter(header, 2);

        if(PrimitiveTypesUtil.asInteger(header, 4) != StunHeader.MAGIC_COOKIE)
            return null;
        final long transactionIdMSB = PrimitiveTypesUtil.asLong(header, 4);
        final long transactionIdLSB = PrimitiveTypesUtil.asLong(header, 12);

        stunHeader.setMessageMethod(method);
        stunHeader.setMessageClass(StunHeader.MessageClass.SUCCESS_RESPONSE);
        stunHeader.setTransactionId(transactionIdMSB, transactionIdLSB);

        StunMessage stunMessage = new StunMessage(stunHeader, new HashMap<>());
        final byte[] payload = inputStream.readNBytes(len);
        if(payload.length != len) {
            return null;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(payload);
        while(bais.available() > 0) {
            TLV rawAttribute = TLV.parse(bais);
            final Class<?> attributeClass;
            try {
                attributeClass = AttributeRegistry.getAttributeClass(rawAttribute);
            } catch (IllegalArgumentException e) {
                if(rawAttribute.getType() < 0x8000) {
                    throw e;
                }
                else {
                    continue;
                }
            }
            if(attributes.contains(attributeClass)) {
                final AttributeRegistry.Serializer<?> serializer =
                        AttributeRegistry.getAttributeSerializer(attributeClass);
                stunMessage.addAttribute(serializer.deserialize(rawAttribute, stunHeader));
            }
        }
        return stunMessage;
    }
}
