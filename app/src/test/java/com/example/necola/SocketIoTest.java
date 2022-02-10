package com.example.necola;

import static org.junit.Assert.assertEquals;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketIoTest {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        } catch (URISyntaxException e) {}
    }




}
