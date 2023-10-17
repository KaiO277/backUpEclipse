package chatRoommain;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UDPChatServer {
    private Map<InetAddress, Integer> clients;
    private DatagramSocket socket;

    public UDPChatServer(int port) throws IOException {
        socket = new DatagramSocket(port);
        clients = new HashMap<>();
        System.out.println("Server started on port " + port);
    }

    public void start() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                if (!clients.containsKey(clientAddress)) {
                    clients.put(clientAddress, clientPort);
                    System.out.println("New client connected: " + clientAddress);
                }

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received from " + clientAddress + ": " + message);

                forwardMessage(clientAddress, clientPort, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void forwardMessage(InetAddress senderAddress, int senderPort, String message) throws IOException {
        byte[] data = message.getBytes();
        for (Map.Entry<InetAddress, Integer> entry : clients.entrySet()) {
            InetAddress clientAddress = entry.getKey();
            int clientPort = entry.getValue();

            // Don't send the message back to the sender
            if (!clientAddress.equals(senderAddress) || clientPort != senderPort) {
                DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
                socket.send(packet);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UDPChatServer server = new UDPChatServer(8888); // Specify the server port
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}