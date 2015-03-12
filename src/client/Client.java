package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {

    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;

    public Client(String host) {
        super("Client");
        chatServer = host;
        enterField = new JTextField();
        enterField.setEditable(false);
        enterField.addActionListener((ActionEvent e) -> {
            sendData(e.getActionCommand());
            enterField.setText("");
        });
        add(enterField, BorderLayout.NORTH);
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(350, 200);
        setVisible(true);
        displayArea.setEditable(false);
    }

    public void runClient() throws IOException {
        try {
            connectToServer();
            getStreams();
            processConnection();
        } catch (EOFException ex) {
            displayMessage("\nClient terminated connection");
        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        displayMessage("Attempting connection\n");

        client = new Socket(InetAddress.getByName(chatServer), 12345);
        displayMessage("Connected to: " + client.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        displayMessage("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        setTextFieldEditable(true);

        do {
            try {
                message += input.readObject();
                displayMessage("\n" + message);
            } catch (ClassNotFoundException exc) {
                displayMessage("\nUnknown object received");
            }
        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    private void closeConnection() {
        displayMessage("\nClosing connection");
        setTextFieldEditable(false);
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ec) {
            System.out.println(ec.getMessage());
        }
    }

    private void sendData(String msg) {
        try {
            output.writeObject("CLIENT>>> " + msg);
            output.flush();
            displayMessage("\nCLIENT>>> " + msg);
        } catch (IOException ex) {
            displayArea.append("Error: " + ex.getMessage());
        }
    }

    private void displayMessage(final String msgToDisplay) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(msgToDisplay);
        });
    }

    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(() -> {
            enterField.setEditable(editable);
        });
    }
}
