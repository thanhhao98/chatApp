package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private final String serverName;
    private final int serverPort;
    private static Socket socket;
    private OutputStream serverOut;
    private InputStream serverIn;
    private static BufferedReader bufferedIn;
    private FileOutputStream fileOutputStream;
    private BufferedOutputStream bufferedOutputStream;

    private ArrayList<MessageListener> messListeners = new ArrayList<>();
    private ArrayList<ServerListener> serverListeners = new ArrayList<>();
    private  ArrayList<FileListener> fileListeners = new ArrayList<>();
    
    public static String toClient;

    public Client(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8890);
        client.addMessageListener(new MessageListener()  {
            @Override
            public void onMessage(String fromClient, String body){
                System.out.println("You got a message from " + fromClient + " :" + body);
            }
        });
        client.addServerListener(new ServerListener() {
            @Override
            public void onRespond(String Respond){
                System.out.println(Respond);
            }
        });
        client.addFileListener(new FileListener() {
            @Override
            public void onRevFile(String revFrom,String nameFile,int sizeFile) throws IOException {
                String pathSave = "/Users/mpxt2/bk/computerNetwork/Ass/demoFileTranfer/"+nameFile;
                client.saveFileRev(pathSave,sizeFile);
            }
        });
        if (!client.connect()) {
            System.err.println("Connnect fail");
        } else {
            System.out.println("Connect successfully");
            if(client.login("thanhhao","977463")){
                System.out.println("login successfully");
            } else {
                System.out.println("error");
            }
        }
    }

    public void saveFileRev(String pathSave,int sizeFile) throws IOException {
        byte [] mybytearray  = new byte [sizeFile];
        this.fileOutputStream = new FileOutputStream(pathSave);
        this.bufferedOutputStream = new BufferedOutputStream(this.fileOutputStream);
        int bytesRead = this.serverIn.read(mybytearray, 0, sizeFile);
        int current = bytesRead;
        do {
            System.out.println("Rev file running... with bytesRead " + bytesRead + " current " + current);
            bytesRead = this.serverIn.read(mybytearray, current, (sizeFile-current));
            if(bytesRead > 0) current += bytesRead;
        } while(bytesRead >0);
        this.bufferedOutputStream.write(mybytearray, 0 , sizeFile);
        this.bufferedOutputStream.flush();
        System.out.println("Rev file successfully");
    }

    public boolean login(String username, String password) throws IOException {
        String cmd = "login " + username + " " + password;
        this.sendCmd(cmd);
        if(this.respondSuccess()){
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean register(String username, String password) throws IOException {
        String cmd = "register " + username + " " + password;
        this.sendCmd(cmd);
        return this.respondSuccess();
    }

    private void startMessageReader() {
        Thread t = new Thread(){
            @Override
            public void run(){
                readerMessageLoop();
            }
        };
        t.start();
    }

    private void readerMessageLoop(){
        String line;
        try {
            while((line=this.bufferedIn.readLine())!=null){
                String[] tokens = line.split("\\s");
                if (tokens!=null && tokens.length >0) {
                    String cmd = tokens[0];
                    if(cmd.equalsIgnoreCase("recv")){
                        handleMessage(line);
                    } else if(cmd.equalsIgnoreCase("recvfile")) {
                        handleSendFile(tokens[1],tokens[2],Integer.parseInt(tokens[3]));
                    }
                    else {
                        handleServerMessage(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } {

        }
    }


    public void handleSendFile(String sendFrom,String nameFile, int sizeFile) throws IOException {
        for (FileListener listener: this.fileListeners){
            listener.onRevFile(sendFrom,nameFile,sizeFile);
        }

    }

    private void handleServerMessage(String line) {
        for (ServerListener listener : this.serverListeners) {
            listener.onRespond(line);
        }

    }

    private void handleMessage(String line) {
        try {
            String sendFrom = line.split("\\s", 3)[1];
            String body = line.split("\\s", 3)[2];
            for (MessageListener listener : this.messListeners) {
                listener.onMessage(sendFrom, body);
            }
        } finally {

        }
    }


    public void sendCmd(String cmd) throws IOException {
        cmd = cmd + "\n";
        this.serverOut.write(cmd.getBytes());
    }

    public String getRespond() throws IOException {
        return this.bufferedIn.readLine();
    }

    public  boolean respondSuccess() throws IOException {
        return this.getRespond().equalsIgnoreCase("successfully");
    }


    public boolean connect() {
        try {
            this.socket = new Socket(this.serverName,this.serverPort);
            System.out.println("Client port is " + this.socket.getLocalPort());
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

    public void close() throws IOException {
        this.socket.close();
    }

    public void addFileListener(FileListener listener){
        this.fileListeners.add(listener);
    }

    public void removeFileListener(FileListener listener){
        this.fileListeners.remove(listener);
    }

    public void addServerListener(ServerListener listener){
        this.serverListeners.add(listener);
    }

    public void removeServerListener(ServerListener listener){
        this.serverListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener){
        this.messListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener){
        this.messListeners.remove(listener);
    }
}
