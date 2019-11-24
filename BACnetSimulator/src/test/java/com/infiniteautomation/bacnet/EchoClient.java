package com.infiniteautomation.bacnet;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class EchoClient {
    public static final int PORT = 48707;
    private DatagramSocket socket;
    private InetAddress address, broadcastAddress;

    private byte[] buf;

    public EchoClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("192.168.1.31");
        broadcastAddress = InetAddress.getByName("192.168.1.255");
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, PORT);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void broadcast(String msg) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = msg.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, broadcastAddress, PORT);
        socket.send(packet);
    }

    List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args) {
        EchoClient echoClient = null;
        try {
            echoClient = new EchoClient();
//            String received = echoClient.sendEcho("Hei fra Ubuntu");
//            System.out.println("Received from Server: " + received);
//            Thread.sleep(1000);
            System.out.println("Send broadcast");
            echoClient.broadcast("Hei til alle");
            Thread.sleep(10000);
            echoClient.close();
            System.out.println("Stopping");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
