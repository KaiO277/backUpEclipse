package chatRoommain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.*;

public class UDPChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private DatagramSocket socket;
    private int clientPort;

    public UDPChatClient(int clientPort) throws SocketException {
        this.clientPort = clientPort;

        setTitle("UDP Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    messageField.setText("");
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void start() {
        try {
            socket = new DatagramSocket(clientPort);

            // Start a separate thread to receive messages
            Thread receiverThread = new Thread(new MessageReceiver());
            receiverThread.start();

            setVisible(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8888); // Specify the server address and port
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UDPChatClient client = new UDPChatClient(9999); // Specify the client port
                    client.start();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}