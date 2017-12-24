import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleChatClient {
    JTextField outgoingMessages; //Editable Text entering region
    PrintWriter writer; //used to pull string from server
    JTextArea incoming; // display needed to display
    BufferedReader reader; //used to store strings sent to server
    Socket sock; //socket

    /*
    -----------------------------------------GUI INITIALIZATION-----------------------------------------
     */

    public void start() { //starting the client GUI
        JFrame chatFrame = new JFrame("Chatbox");   // DIMENTIONS OF THE JFRAME
        JPanel panel = new JPanel();
        JButton sendButton = new JButton("send"); //send button initialization
        outgoingMessages = new JTextField(20); // textfield for entering messages to the server
        incoming = new JTextArea(15,50);
        incoming.setEditable(false);
        incoming.setVisible(true);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        outgoingMessages.setText(" ");

        JScrollPane scroller = new JScrollPane(incoming);
        sendButton.addActionListener(new SendButtonListener());
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //--adding elements to GUI Swing panel
        panel.add(outgoingMessages);
        panel.add(sendButton);
        panel.add(scroller);

        //trigger server connection  (REASON WHY SERVER IS RUN BEFORE CLIENT)
        setUpNetworking();

        //Initializing reader thread
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        chatFrame.getContentPane().add(BorderLayout.CENTER, panel);
        chatFrame.setSize(400,500);
        chatFrame.setVisible(true);


    }


    public void setUpNetworking () {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream()); //retrieving low-level bytes into an streamReader
            reader = new BufferedReader(streamReader); //buffer reader takes low-level bytes from streamReader (input reader) and converts it into strings
            writer = new PrintWriter(sock.getOutputStream()); //Print writer convers that srings we are writing into bytes into the Socket
            System.out.println("Connection Secured..."); //connection secured when BOTH are initialized
        }
        catch (IOException e ){
            e.printStackTrace();        }
    }

    public class SendButtonListener implements ActionListener { //action listener that implements the action triggered by send button
        public void actionPerformed(ActionEvent ev) {
            try { //try for Printwriter action
            writer.println(outgoingMessages.getText()); //now that it is initialized and connected to the server, anything in outgoing textfield is sent to the server
            writer.flush(); //clear out data
        } catch (Exception ex) {
            ex.printStackTrace();
            }
            outgoingMessages.requestFocus();
        }

    }

    public class IncomingReader implements Runnable{ //Thread job: which is to read messages from chat
        public void run(){
            String message;  // string from server is saved into
                try {
                    while ((message = reader.readLine()) != null) { //while there is something in the chatbox being entered
                        System.out.println("Read " + message); //print it
                        incoming.append(message + "\n"); //put it in the chatbox window
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


