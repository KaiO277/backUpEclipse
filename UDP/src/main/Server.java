package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
	public static void main(String[] args) throws IOException {
		System.out.println("Server is running..");
		DatagramSocket server = new DatagramSocket(1234);
		byte[]buff = new byte[1024];
		DatagramPacket recevie = new DatagramPacket(buff, 1024);
		server.receive(recevie);
		byte[]recevieByte = recevie.getData();
		String receiveString = new String(recevieByte, 0 , recevie.getLength());
		if(receiveString.equals("send")) {
			System.out.println("Da nhan");
		}
		server.close();
	}

}
