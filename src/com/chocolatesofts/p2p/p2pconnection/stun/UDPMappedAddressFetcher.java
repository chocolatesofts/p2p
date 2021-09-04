package com.chocolatesofts.p2p.p2pconnection.stun;

import com.chocolatesofts.p2p.p2pconnection.stun.methods.BindingMethod;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;
import com.chocolatesofts.p2p.p2pconnection.stun.protocol.StunMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPMappedAddressFetcher {
    private AddressHolder addressHolder;
    private char sourcePort;
    private int timeout;
    private String host;
    private char port;
    public UDPMappedAddressFetcher(final char sourcePort, final int timeout, final String host, final char port) {
        this.sourcePort = sourcePort;
        this.timeout = timeout;
        this.host = host;
        this.port = port;
    }

    public void fetch() throws IOException {
        if(isAddressPresent())
            throw new IllegalArgumentException("Already fetched");
        DatagramSocket datagramSocket = new DatagramSocket(sourcePort);
        if(timeout>0)
            datagramSocket.setSoTimeout(timeout);

        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        StunMessage requestMessage = BindingMethod.newRequestPayload(payload);
        payload.flush();
        byte[] payloadBytes = payload.toByteArray();
        DatagramPacket stunPacket = new DatagramPacket(payloadBytes, payloadBytes.length,
                InetAddress.getByName(host), port);
        byte[] bufferPacket = new byte[2048];
        DatagramPacket receivePacket = new DatagramPacket(bufferPacket, 2048);

        datagramSocket.send(stunPacket);
        datagramSocket.receive(receivePacket);

        ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength());
        StunMessage responseMessage = BindingMethod.deserializeResponsePayload(bais);
        if(responseMessage.sameTransaction(requestMessage)) {
            addressHolder = BindingMethod.getAddress(responseMessage);
        } else {
            throw new IOException("wrong packet received");
        }
    }

    public boolean isAddressPresent() {
        return addressHolder != null;
    }

    public AddressHolder getAddress() {
        if(!isAddressPresent())
            throw new IllegalStateException("Address not fetched yet");
        return addressHolder;
    }

    public void reset() {
        addressHolder = null;
    }

    public void reset(final char sourcePort, final int timeout) {
        reset();
        this.sourcePort = sourcePort;
        this.timeout = timeout;
    }

    public void reset(final char sourcePort, final int timeout, final String host, final char port) {
        reset(sourcePort, timeout);
        this.host = host;
        this.port = port;
    }
}
