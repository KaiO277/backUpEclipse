package main;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
    	System.out.println("Server is running...");
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(9876); // Mở cổng 9876 để lắng nghe dữ liệu từ clients

            while (true) {
                byte[] receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Nhận dữ liệu từ client

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + receivedMessage);

                // Xử lý dữ liệu và tính toán
                String[] parts = receivedMessage.split(",");
                if (parts.length != 2) {
                    System.err.println("Invalid data format");
                    continue;
                }

                try {
                    int a = Integer.parseInt(parts[0]);
                    int b = Integer.parseInt(parts[1]);
                    int result = a - b;

                    // Lưu kết quả vào danh sách
                    List<Integer> resultList = new ArrayList<>();
                    resultList.add(result);

                    // Chuẩn bị dữ liệu để gửi về client
                    String response = "Result: " + result;
                    byte[] responseData = response.getBytes();
                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();
                    DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);

                    // Gửi kết quả về client
                    socket.send(sendPacket);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numbers received");
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
