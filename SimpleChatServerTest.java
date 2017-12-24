import com.sun.security.ntlm.Server;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

//Server SIDE

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleChatServerTest {
    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable { //Server Thread 
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Read " + message);
                    tellEveryone(message);
                }
            } catch (IOException ec) {
                ec.printStackTrace();
            }
        }
    }


    public void go() {

        clientOutputStreams = new ArrayList();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket clientSOcket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSOcket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSOcket));
                t.start();
                System.out.println("...Achieved a Connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void tellEveryone(String content) {
            Iterator it = clientOutputStreams.iterator();
            while (it.hasNext()) {
                try {
                    PrintWriter writer = (PrintWriter) it.next();
                    writer.println(content);
                    writer.flush();

                } catch (Exception exc) {
                    exc.printStackTrace();

                }
            }
        }
}

