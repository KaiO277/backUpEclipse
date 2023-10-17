package main;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UDPClient {
    private static DatagramSocket socket = null;
    private static JTextArea messageTextArea;
    private static JTextField messageTextField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UDP Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());

        messageTextArea = new JTextArea();
        messageTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        messageTextField = new JTextField();
        panel.add(messageTextField, BorderLayout.SOUTH);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(panel);

        frame.setVisible(true);

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 9876;

            Thread receiveThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        receiveMessage();
                    }
                }
            });
            receiveThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendMessage() {
        String message = messageTextField.getText();
        if (!message.isEmpty()) {
            try {
                InetAddress serverAddress = InetAddress.getByName("localhost");
                int serverPort = 9876;
                byte[] sendData = (InetAddress.getLocalHost().getHostAddress() + ":" + socket.getLocalPort() + " - " + message).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                socket.send(sendPacket);
                messageTextField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private static void receiveMessage() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            messageTextArea.append(receivedMessage + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
