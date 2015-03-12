

package networking;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class ServerMain {
  public static void main(String[] args){
      
        ServerGUI app = new ServerGUI();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        app.runServer();
     
  }  
}
