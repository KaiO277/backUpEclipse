package main;
import java.net.*;
import java.util.*;

public class UDPServer {
    private static DatagramSocket socket = null;
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static List<Integer> clientPorts = new ArrayList<>();

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(9876); // Mở cổng 9876 để lắng nghe dữ liệu từ clients

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

                // Gửi lại tin nhắn cho tất cả clients
                for (int i = 0; i < clientAddresses.size(); i++) {
                    InetAddress address = clientAddresses.get(i);
                    int port = clientPorts.get(i);

                    byte[] sendData = receivedMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                    System.out.println(port+" "+ address);
                    socket.send(sendPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
