package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
	public static void main(String[] args) throws IOException {
		DatagramSocket client = new DatagramSocket();
		String send = "send";
		byte[] sendByte = send.getBytes();
		InetAddress remoteAddress = InetAddress.getByName("localhost");
		DatagramPacket datagramPacket = new DatagramPacket(sendByte, sendByte.length, remoteAddress, 1234);
		client.send(datagramPacket);
		client.close();
	}

}
