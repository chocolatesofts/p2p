package com.chocolatesofts.p2p.p2pconnection.holepunch.test;

import com.chocolatesofts.p2p.p2pconnection.holepunch.AddressEncodeDecoder;
import com.chocolatesofts.p2p.p2pconnection.holepunch.TCPP2PConnection;
import com.chocolatesofts.p2p.p2pconnection.stun.UDPMappedAddressFetcher;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;

import java.io.*;
import java.net.Socket;

public class Main {
    static InputStream inputStream;
    static OutputStream outputStream;
    static long totalReadSize = 0;
    static long totalWriteSize = 0;

    final static Runnable readRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                byte[] buffer = new byte[1024*1024];
                try {
                    int lenRead = inputStream.read(buffer);
                    if(lenRead < 0)
                        throw new IOException("Read size negative");
                    totalReadSize += lenRead;
                } catch (IOException e) {
                    System.err.println("Read loop error");
                    System.exit(-1);
                }
            }
        }
    };

    final static Runnable writeRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                byte[] buffer = new byte[1024 * 256];
                try {
                    for(int i=0; i< buffer.length; i++) {
                        buffer[i] = (byte)(Math.random() * 256);
                    }
                    outputStream.write(buffer);
                    totalWriteSize += buffer.length;
                } catch (IOException e) {
                    System.err.println("Write loop error");
                    System.exit(-1);
                }
            }
        }
    };

    final static Runnable outputRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                long lastTotalReadSize = 0;
                long lastTotalWriteSize = 0;
                long begin = System.currentTimeMillis();
                while(true) {
                    Thread.sleep(1000);
                    long delTotalReadSize = totalReadSize - lastTotalReadSize;
                    long delTotalWriteSize = totalWriteSize - lastTotalWriteSize;
                    String readStat = String.format("Read : %dMB (%dKB/s , %dKB/s)", totalReadSize>>20, (delTotalReadSize>>10), totalReadSize/(System.currentTimeMillis()-begin));
                    String writeStat = String.format("Write : %dMB (%dKB/s , %dKB/s)", totalWriteSize>>20, (delTotalWriteSize>>10), totalWriteSize/(System.currentTimeMillis()-begin));
                    System.out.println(String.format("%s\t%s", readStat, writeStat));
                    lastTotalReadSize += delTotalReadSize;
                    lastTotalWriteSize += delTotalWriteSize;
                }
            } catch (Exception e) {
                System.err.println("Output loop error");
                System.exit(-1);
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        final char port = (char)((Math.random() * 32768) + 2000);
        final UDPMappedAddressFetcher addressFetcher = new UDPMappedAddressFetcher(port,-1,"stun.qq.com", (char)3478);
        final TCPP2PConnection tcpp2PConnection = new TCPP2PConnection(addressFetcher);
        final TCPP2PConnection.ConnectionListener connectionListener = new TCPP2PConnection.ConnectionListener() {
            @Override
            public void onMap(final AddressHolder addressHolder) {
                final String selfCode = AddressEncodeDecoder.encode(addressHolder);
                System.out.println("Your code : " + selfCode);
                System.out.print("Enter code : ");
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    final String code = bufferedReader.readLine();
                    if(code.equals(selfCode)) {
                        System.out.println("The codes cannot be same");
                        System.exit(-1);
                    }
                    tcpp2PConnection.resumeConnect(AddressEncodeDecoder.decode(code));
                } catch (final Exception e) {
                    System.out.println("Error");
                    System.exit(-1);
                }
            }

            @Override
            public void onConnect(final Socket socket) {
                System.out.println("Connected");
                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    System.err.println("Error getting IO streams");
                    System.exit(-1);
                }

                new Thread(readRunnable).start();
                new Thread(writeRunnable).start();
                new Thread(outputRunnable).start();
            }

            @Override
            public void onDisconnect() {

            }

            @Override
            public void onException(final Exception e, final TCPP2PConnection.State prevState) {
                System.out.println("Error");
                System.exit(-1);
            }
        };
        tcpp2PConnection.setConnectionListener(connectionListener);
        tcpp2PConnection.connect();
    }
}
