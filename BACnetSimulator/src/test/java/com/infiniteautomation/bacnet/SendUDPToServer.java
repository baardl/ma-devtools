package com.infiniteautomation.bacnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendUDPToServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(48707);
        InetAddress serverAddress = InetAddress.getByName("192.18.1.31");
        byte[] buf = "Hei fra Ubuntu".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, socket.getLocalPort());
        socket.send(packet);
    }
}
