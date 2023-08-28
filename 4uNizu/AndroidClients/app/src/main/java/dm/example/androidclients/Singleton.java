package dm.example.androidclients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Singleton {

    // Static variable reference of single_instance
    // of type Singleton
    private static Singleton single_instance = null;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBr() {
        return br;
    }

    public PrintWriter getPw() {
        return pw;
    }

    // Declaring a variable of type String
    //public String s;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private Singleton(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        if (this.socket == null) {
            this.single_instance = null;
        }
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized Singleton getInstance(String address, int port) throws IOException {
        if (single_instance == null) {
            single_instance = new Singleton(address, port);
        }
        return single_instance;
    }
}