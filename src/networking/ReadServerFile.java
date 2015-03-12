
package networking;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JApplet;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReadServerFile extends JFrame{

   private JTextField enterField;
   private JEditorPane contentArea;
   
   public ReadServerFile(){
       super("Web Browser");
       
       enterField = new JTextField("Enter file URL here");
       enterField.addActionListener((ActionEvent e) -> {
           getThePage(e.getActionCommand());
       });
       add(enterField, BorderLayout.NORTH);
       
       contentArea = new JEditorPane();
       contentArea.setEditable(false);
       contentArea.addHyperlinkListener((HyperlinkEvent e) -> {
           if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
               getThePage(e.getURL().toString());
       });
       add(new JScrollPane(contentArea), BorderLayout.CENTER);
       setSize(400, 300);
       setVisible(true);
   }
    private void getThePage(String location){
        try{
            contentArea.setPage(location);
            enterField.setText(location);
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage());
           // JOptionPane.showInternalMessageDialog(rootPane, ex.getMessage());
        }
    }
    public static void main(String[] args){
        ReadServerFile net = new ReadServerFile();
        net.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
