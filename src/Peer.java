import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Peer {
    private ServerSocket serverSocket;
    private List<Socket> neighbours;

    public Peer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        neighbours = new ArrayList<>();
    }

    public void connect(int targetPort) throws IOException {

    }
}
