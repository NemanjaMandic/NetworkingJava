
package client;

import java.io.IOException;
import javax.swing.JFrame;


public class ClientMain {
   public static void main(String[] args) throws IOException{
       ClientGUI cl = new ClientGUI("127.0.0.1");
       cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       cl.setLocationRelativeTo(null);
       cl.setVisible(true);
       cl.runClient();
   } 
}
