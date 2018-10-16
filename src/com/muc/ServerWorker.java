package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private Boolean login = null;
    private OutputStream outputStream;
    private String username;
    private DataClient client;

    public ServerWorker(Server server,Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run(){
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine())!= null){
            String[] tokens = line.split("\\s");
            if (tokens!=null && tokens.length >0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(cmd)) {
                    handleLogout();
                } else if ("login".equalsIgnoreCase(cmd)){
                    handleLogin(this.outputStream, tokens);
                } else if("listOnline".equalsIgnoreCase(cmd)) {
                    handleListOnline();
                } else {
                    String msg = "Unknown type: " + line + "\n";
                    this.outputStream.write(msg.getBytes());
                }
            }
        }
        this.clientSocket.close();
    }

    private void handleListOnline() throws IOException {
        ArrayList<ServerWorker> workerList = getWorkerList();
        for(ServerWorker worker: workerList ){
            String onlMsg2 = "Online " + worker.username + "\n";
            this.send(onlMsg2);
        }
    }

    private void handleLogout() throws IOException {
        ArrayList<ServerWorker> workerList = getWorkerList();
        this.send("You are logout\n");
        for (ServerWorker worker: workerList){
            String logoutMsg = "Offline " + this.username + "\n";
            worker.send(logoutMsg);
        }
        this.server.removeWorker(this);
        this.clientSocket.close();
    }

    private ArrayList<ServerWorker> getWorkerList() {
        return this.server.getWorkerList();
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        String msg = null;
        if(tokens.length==3){
            this.username = tokens[1];
            String password = tokens[2];
            if((username.equals("guest") && password.equals("guest"))||(username.equals("thanhhao") && password.equals("977463"))){
                this.login = true;
                ArrayList<ServerWorker> listWorker = getWorkerList();
                for(ServerWorker worker: listWorker){
                    if(this.username.equals(worker.username)){
                        worker.handleLogout();
                    }
                }
                this.server.addListWorker(this);
                msg = "Longin ok\n";
            } else {
                msg = "Error login\n";
            }
        } else {
            msg = "Error login\n";
        }
        outputStream.write((msg.getBytes()));

    }

    private void send(String msg) throws IOException {
        this.outputStream.write(msg.getBytes());
    }

    public boolean getLogin(){
        return this.login;
    }
}
