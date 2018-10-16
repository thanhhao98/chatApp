package com.muc;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String serverName;
    private final int serverPort;
    private static Socket socket;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader bufferedIn;

    public Client(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8890);
        if (!client.connect()) {
            System.err.println("Connnect fail");
        } else {
            System.out.println("Connect successfully");
            client.login("thanhhao","977463");
        }
    }

    private void login(String username, String password) throws IOException {
        String cmd = "login " + username + " " + password + "\n";
        this.serverOut.write(cmd.getBytes());
        String response = this.bufferedIn.readLine();
        System.out.println("Respond line: " + response);

    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName,serverPort);
            System.out.println("Client address is " + this.socket.getLocalAddress());
            this.serverOut = this.socket.getOutputStream();
            this.serverIn = this.socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(this.serverIn));
            System.out.println("Create bufferIn successfully");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
