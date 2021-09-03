package com.chocolatesofts.p2p.p2pconnection.stun.protocol;

import com.chocolatesofts.p2p.p2pconnection.stun.util.PrimitiveTypesUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class StunHeader {
    public static class NonInitalizedException extends IllegalStateException {
        final String fieldName;
        NonInitalizedException(final String filedName) {
            super();
            this.fieldName = filedName;
        }
    }

    public static final long MAGIC_COOKIE = 0x2112A442;
    private Long transactionIdMSB, transactionIdLSB;
    private Character messageMethod;
    public enum MessageClass {
        REQUEST,
        SUCCESS_RESPONSE,
        ERROR_RESPONSE,
        INDICATION
    }
    private MessageClass messageClass;

    public void setTransactionId(final int id1, final long id2) {
        transactionIdMSB = (MAGIC_COOKIE<<32) + PrimitiveTypesUtil.unsignedInteger(id1);
        transactionIdLSB = id2;
    }

    public void setTransactionId(final long id1, final long id2) {
        transactionIdMSB = id1;
        transactionIdLSB = id2;
    }

    public void setMessageClass(final MessageClass messageClass) {
        this.messageClass = messageClass;
    }

    public MessageClass getMessageClass() {
        return  messageClass;
    }

    public void randomizeTransactionId() {
        setTransactionId((int)(Math.random()*(1L<<32)),
                (long)(Math.random()*Long.MAX_VALUE + Math.random()*Long.MIN_VALUE));
    }

    public void setMessageMethod(final char messageMethod) {
        this.messageMethod = messageMethod;
    }

    public void serializeTransacrionId(final DataOutputStream outputStream) throws IOException {
        outputStream.writeLong(transactionIdMSB);
        outputStream.writeLong(transactionIdLSB);
    }

    public void serializeMessageType(final DataOutputStream outputStream) throws IOException {
        if(messageMethod == null)
            throw new NonInitalizedException("method");
        if(messageClass == null)
            throw new NonInitalizedException("class");

        byte typeMSB = 0, typeLSB = 0;

        if(messageClass==MessageClass.ERROR_RESPONSE || messageClass==MessageClass.SUCCESS_RESPONSE) {
            typeMSB |= 0x01;
        }

        if(messageClass==MessageClass.ERROR_RESPONSE || messageClass==MessageClass.INDICATION) {
            typeLSB |= 0x10;
        }
        typeLSB |= (messageMethod&0x0F);
        outputStream.writeByte(typeMSB);
        outputStream.writeByte(typeLSB);
    }

    public boolean sameTransaction(final StunHeader header) {
        if(header.transactionIdLSB == null || header.transactionIdMSB == null)
            return false;
        return header.transactionIdLSB.equals(transactionIdLSB) && header.transactionIdMSB.equals(transactionIdMSB);
    }
}
