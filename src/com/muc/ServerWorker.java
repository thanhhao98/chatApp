package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    private boolean login = false;
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
                if(!this.login){
                    if ("quit".equalsIgnoreCase(cmd)) {
                        this.clientSocket.close();
                    } else if ("login".equalsIgnoreCase(cmd)) {
                        handleLogin(tokens);
                    } else if("register".equalsIgnoreCase(cmd)){
                            handleRegister(tokens[1],tokens[2]);
                    } else {
                        sendErrorMessage();
                    }
                } else {
                    if("listOnline".equalsIgnoreCase(cmd)) {
                        handleListOnline();
                    } else if("send".equalsIgnoreCase(cmd)) {
                        try {
                            String body = line.split("\\s", 3)[2];
                            handleSendMessage(tokens[1], body);
                        } finally {
                        }
                    } else if("filesend".equalsIgnoreCase(cmd)){
                        this.sendSuccessMessage();
//                        handleFileSend(tokens[1],tokens[2]);

                    } else if("logout".equalsIgnoreCase(cmd)){
                        this.handleLogout();
                    } else {
                        sendErrorMessage();
                    }
                }
            }
        }
        this.clientSocket.close();
    }

    //Not ok

//    private void handleFileSend(String username, String pathFile) throws IOException {
//        File sendFile = new File(pathFile);
//        try {
//            byte [] myByteArray = new byte[(int)sendFile.length()];
//            this.fileInputStream = new FileInputStream(sendFile);
//            this.bufferedInputStream = new BufferedInputStream(this.fileInputStream);
//            this.bufferedInputStream.read(myByteArray,0,myByteArray.length);
//            boolean sendSuccess = false;
//            DataClient revClient = new DataClient(username,null);
//            ArrayList<ServerWorker> workerList = getWorkerList();
//            for (ServerWorker worker: workerList){
//                if(!revClient.equals(this.getClient()) && revClient.equals(worker.getClient())){
//                    this.outputStream = worker.server.
//                    sendSuccess = true;
//                }
//            }
//            if(!sendSuccess){
//                this.sendErrorMessage();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            this.sendErrorMessage();
//        }
//    }

    private void handleRegister(String username, String password) throws IOException {
        this.client = new DataClient("100",username,password);
        if(this.client.checkUsernameExist()==null){
            this.client.addDataClient();
            sendSuccessMessage();
        } else {
            sendErrorMessage();
        }
    }


    private void sendErrorMessage() throws IOException {
        this.send("error\n");
    }

    private void sendSuccessMessage() throws IOException{
        this.send("successfully\n");
    }

    private void handleSendMessage(String usernameRev, String body) throws IOException {
        boolean sendSuccess = false;
        DataClient revClient = new DataClient(usernameRev,null);
        ArrayList<ServerWorker> workerList = getWorkerList();
        String msg = "recv " + this.client.getUsername() + " " + body + '\n';
        for (ServerWorker worker: workerList){
            if(!revClient.equals(this.getClient()) && revClient.equals(worker.getClient())){
                worker.send(msg);
                this.sendSuccessMessage();
                sendSuccess = true;
            }
        }
        if(!sendSuccess){
            this.sendErrorMessage();
        }
    }

    private void handleListOnline() throws IOException {
        ArrayList<ServerWorker> workerList = getWorkerList();
        ArrayList<String> listUserOnline = new ArrayList<>();
        for(ServerWorker worker: workerList ) {
            listUserOnline.add(worker.client.getUsername());
        }
        String onlMsg2 = "listonline " + String.join(",",listUserOnline) + "\n";
        this.send(onlMsg2);
    }

    private void handleLogout() throws IOException {
        ArrayList<ServerWorker> workerList = getWorkerList();
        this.sendSuccessMessage();
        this.server.removeWorker(this);
        this.login = false;
    }

    private ArrayList<ServerWorker> getWorkerList() {
        return this.server.getWorkerList();
    }

    private void handleLogin(String[] tokens) throws IOException {
        if(tokens.length==3){
            this.client = new DataClient(tokens[1],tokens[2]);
            if(this.client.checkUserExist()){
                this.login = true;
                ArrayList<ServerWorker> listWorker = getWorkerList();
                for(ServerWorker worker: listWorker){
                    System.out.println(worker.client.getUsername());
                    if(this.client.equals(worker.getClient())){
                        worker.handleLogout();
                    }
                }
                this.server.addListWorker(this);
                this.sendSuccessMessage();
            } else {
                this.sendErrorMessage();
            }
        } else {
            this.sendErrorMessage();
        }
    }

    public DataClient getClient(){
        return this.client;
    }

    private void send(String msg) throws IOException {
        this.outputStream.write(msg.getBytes());
    }

    public boolean getLogin(){
        return this.login;
    }
}
