package main;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new DatagramSocket();

            List<String> messages = new ArrayList<>();
            System.out.print("Enter the number of data sets to send: ");
            int n = scanner.nextInt();

            for (int i = 0; i < n; i++) {
                System.out.print("Enter value for 'a' for data set " + (i + 1) + ": ");
                int a = scanner.nextInt();
                System.out.print("Enter value for 'b' for data set " + (i + 1) + ": ");
                int b = scanner.nextInt();

                String message = a + "," + b;
                messages.add(message);
            }

            InetAddress serverAddress = InetAddress.getByName("localhost"); // Địa chỉ máy chủ
            int serverPort = 9876; // Cổng máy chủ

            for (String message : messages) {
                byte[] sendData = message.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                socket.send(sendPacket); // Gửi dữ liệu đến máy chủ

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Nhận kết quả từ máy chủ

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from server: " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            scanner.close();
        }
    }
}

