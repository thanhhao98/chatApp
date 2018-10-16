package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private final int serverPort;
    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int port) {
        this.serverPort = port;
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true){
                System.out.print("About to accept connection...\n");
                Socket clientSocket = serverSocket.accept();
                System.out.print("Accept connection from"+clientSocket+"\n");
                ServerWorker worker = new ServerWorker(this,clientSocket);
                worker.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addListWorker(ServerWorker worker) {
        this.workerList.add(worker);
    }

    public ArrayList<ServerWorker> getWorkerList(){
        return workerList;
    }

    public void removeWorker(ServerWorker serverWorker) {
        this.workerList.remove(serverWorker);
    }



}
