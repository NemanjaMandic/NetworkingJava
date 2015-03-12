
package networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame{
    
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;
    
    public Server(){
        super("Chat");
        
        enterField = new JTextField();
        enterField.setEditable(false);
        enterField.addActionListener((ActionEvent e) -> {
           sendData(e.getActionCommand());
           enterField.setText("");
        });
        add(enterField, BorderLayout.NORTH);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        setSize(300, 150);
        
        setVisible(true);
    }
    public void runServer(){
        try{
            server = new ServerSocket(12345, 100);
            while(true){
                waitForConnection();
                getStreams();
                processConnection();
            }
        }catch(EOFException ex){
            displayMessage("\nServer terminated connection");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }finally{
            closeConnection();
            ++counter;
        }
    }
    private void  waitForConnection() throws IOException{
        displayMessage("Waiting for connection\n");
        connection = server.accept();
        displayMessage("Connection " + counter + " received from: "+connection.getInetAddress());
    }
    private void getStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        displayMessage("\nGot I/O streams\n");
        
    }
    private void processConnection() throws IOException{
        String message = "Connection successful !";
        sendData(message);
        
        setTextFieldEditable(true);
        
        do{
           try{
             message = (String) input.readObject();
             displayMessage("\n"+message);
           }catch(ClassNotFoundException ex){
               displayMessage("\nUnknown object type received");
           } 
        }while(!message.equals("CLIENT>>> TERMINATE"));
    }
    private void closeConnection(){
        displayMessage("\nTerminated connection\n");
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ex){
            JOptionPane.showMessageDialog(rootPane, ex.getCause().getMessage());
        }
    }
    private void sendData(String message){
        try{
            output.writeObject("SERVER>>> "+message);
            output.flush();
            displayMessage("\nSERVER>>> "+message);
        }catch(IOException ex){
            displayArea.append("\nSome error ocured: "+ ex.getMessage());
        }
    }
    private void displayMessage(final String messageToDisplay){
        SwingUtilities.invokeLater(() -> {
            displayArea.append(messageToDisplay);
        });
    }
    private void setTextFieldEditable(final boolean editable){
        enterField.setEditable(editable);
    }
}
