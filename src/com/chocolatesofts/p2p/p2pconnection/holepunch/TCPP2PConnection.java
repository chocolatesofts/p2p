package com.chocolatesofts.p2p.p2pconnection.holepunch;

import com.chocolatesofts.p2p.p2pconnection.stun.UDPMappedAddressFetcher;
import com.chocolatesofts.p2p.p2pconnection.stun.models.AddressHolder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPP2PConnection {

    public interface ConnectionListener {
        void onMap(AddressHolder addressHolder);
        void onConnect(Socket socket);
        void onDisconnect();
        void onException(Exception e, State prevState);
    }
    private ConnectionListener connectionListener;

    public enum State {
        INITIALIZED,
        MAPPING,
        MAPPED,
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        ERROR
    }
    private State currentState;
    private final Object stateLock;

    private final UDPMappedAddressFetcher mappedAddressFetcher;
    private Socket tcpSocket;
    private AddressHolder remoteAddress;
    private int timeout;

    final Runnable mappedAddressFetcherRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mappedAddressFetcher.fetch();
                synchronized (stateLock) {
                    currentState = State.MAPPED;
                }
                if(connectionListener != null)
                    connectionListener.onMap(mappedAddressFetcher.getAddress());
            } catch (final Exception e) {
                publishException(e);
            }
        }
    };

    final Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                tcpSocket = new Socket();
                tcpSocket.bind(new InetSocketAddress("0.0.0.0", mappedAddressFetcher.getAddress().getPort()));
                if(timeout > 0) {
                    tcpSocket.setSoTimeout(timeout);
                }
                tcpSocket.connect(new InetSocketAddress(remoteAddress.getAddressAsString(), remoteAddress.getPort()));
                synchronized (stateLock) {
                    currentState = State.CONNECTED;
                }
                if(connectionListener != null)
                    connectionListener.onConnect(tcpSocket);
            } catch (final Exception e) {
                publishException(e);
            }
        }
    };

    public TCPP2PConnection(final UDPMappedAddressFetcher udpMappedAddressFetcher) {
        this.mappedAddressFetcher = udpMappedAddressFetcher;
        stateLock = new Object();
        this.currentState = State.INITIALIZED;
    }

    private void publishException(final Exception e) {
        final State prevState = currentState;
        synchronized (stateLock) {
            currentState = State.ERROR;
        }
        if(connectionListener != null)
            connectionListener.onException(e, prevState);
    }

    public void connect() {
        synchronized (stateLock) {
            if(currentState != State.INITIALIZED) {
                throw new IllegalStateException("Not initialized");
            }
            currentState = State.MAPPING;
            new Thread(mappedAddressFetcherRunnable).start();
        }
    }

    public void resumeConnect(final AddressHolder addressHolder, final int timeout) {
        synchronized (stateLock) {
            if(currentState != State.MAPPED) {
                throw new IllegalStateException("Not mapped");
            }
            this.timeout = timeout;
            this.remoteAddress = addressHolder;
            currentState = State.CONNECTING;
            new Thread(connectRunnable).start();
        }
    }

    public void resumeConnect(final AddressHolder addressHolder) {
        resumeConnect(addressHolder, -1);
    }

    public void disconnect() {
        synchronized (stateLock) {
            if(currentState != State.CONNECTED)
                throw new IllegalStateException("Not in connected state");
            try {
                tcpSocket.close();
                currentState = State.DISCONNECTED;
            } catch (IOException e) {
                publishException(e);
            }
        }
        if(connectionListener != null)
            connectionListener.onDisconnect();
    }

    public void reset() {
        synchronized (stateLock) {
            if(currentState == State.CONNECTED)
                disconnect();
            else if(currentState != State.DISCONNECTED && currentState != State.INITIALIZED && currentState != State.ERROR){
                throw new IllegalStateException("Cannot reset");
            }
            mappedAddressFetcher.reset();
            currentState = State.INITIALIZED;
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setConnectionListener(final ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
}
