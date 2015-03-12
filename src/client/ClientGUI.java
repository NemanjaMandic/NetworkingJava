package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientGUI extends javax.swing.JFrame {

    public ClientGUI(String host) {
        chatServer = host;
        initComponents();
        displayArea.setEditable(false);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        ipField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        enterField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("IP Address: ");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));

        displayArea.setColumns(20);
        displayArea.setRows(5);
        jScrollPane1.setViewportView(displayArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        );

        enterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterFieldActionPerformed(evt);
            }
        });
        jScrollPane2.setViewportView(enterField);

        sendButton.setText("SEND");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(sendButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sendButton)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private final String chatServer;
    private Socket client;


    private void enterFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterFieldActionPerformed
        sendData(evt.getActionCommand());
        enterField.setText("");
    }//GEN-LAST:event_enterFieldActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if (enterField != null) {
            String msg = enterField.getText();
            sendData(msg);
            enterField.setText("");
        }
    }//GEN-LAST:event_sendButtonActionPerformed
    public void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();
        } catch (EOFException ex) {
            displayMessage("\nClient Terminated connection");
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(rootPane, "ERROR: " + exc.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        displayMessage("\nAttempting connection\n");
        client = new Socket(InetAddress.getByName(chatServer), 12345);

        displayMessage("\nConnected to: " + client.getInetAddress().getHostName());
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
            } catch (ClassNotFoundException ex) {
                displayMessage("\nSome error occured: " + ex.getMessage());
            }
        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    private void closeConnection() {
        displayMessage("\nClosing connection.");
        setTextFieldEditable(false);
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendData(String message) {
        try {
            output.writeObject("\nCLIENT >>> " + message);
            output.flush();
            displayMessage("\nCLIENT >>> " + message);
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(rootPane, "ERROR: " + exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMessage(final String msg) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(msg);
        });
    }

    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(() -> {
            enterField.setEditable(editable);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea displayArea;
    private javax.swing.JTextField enterField;
    private javax.swing.JTextField ipField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
