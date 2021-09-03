package com.chocolatesofts.p2p.p2pconnection.stun.test;

import com.chocolatesofts.p2p.p2pconnection.stun.UDPMappedAddressFetcher;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) throws IOException {
        final char port = (char)((Math.random() * 32768) + 2000);
        UDPMappedAddressFetcher addressFetcher = new UDPMappedAddressFetcher(port, 2000, "stun.qq.com", (char)3478);
        addressFetcher.fetch();

        System.out.println("Source port : " + (int)port);
        System.out.println("Mapped Address : " +  addressFetcher.getAddress());
    }
}
