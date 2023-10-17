package main;

import java.net.*;
import java.util.*;

public class P2PChatServer {
    private static DatagramSocket socket = null;
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static List<Integer> clientPorts = new ArrayList<>();
    private static List<String> messages = new ArrayList<>(); // Danh sách tin nhắn từ clients

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(9876); // Mở cổng 9876 để lắng nghe dữ liệu từ clients

            // Luồng cập nhật tin nhắn trên giao diện các client
            Thread updateThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        sendMessagesToClients();
                        try {
                            Thread.sleep(1000); // Cập nhật mỗi giây
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            updateThread.start();

            while (true) {
                byte[] receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Nhận dữ liệu từ client

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Để đảm bảo mỗi client chỉ được thêm một lần
                if (!clientAddresses.contains(clientAddress) || !clientPorts.contains(clientPort)) {
                    clientAddresses.add(clientAddress);
                    clientPorts.add(clientPort);
                    System.out.println("Client connected: " + clientAddress + ":" + clientPort);
                }

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + receivedMessage);

                // Lưu trữ tin nhắn từ client
                messages.add(receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void sendMessagesToClients() {
        try {
            for (int i = 0; i < clientAddresses.size(); i++) {
                InetAddress address = clientAddresses.get(i);
                int port = clientPorts.get(i);

                for (String message : messages) {
                    byte[] sendData = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                    socket.send(sendPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
