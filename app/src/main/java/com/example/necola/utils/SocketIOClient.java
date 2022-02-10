package com.example.necola.utils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOClient {

    private static Socket mSocket;

    private static void initSocket() {
        try {
            mSocket = IO.socket("http://159.75.16.150:8000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Socket getInstance() {
        if (mSocket != null) {
            return mSocket;
        } else {
            initSocket();
            return mSocket;
        }
    } }