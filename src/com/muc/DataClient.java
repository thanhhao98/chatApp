package com.muc;

import java.util.ArrayList;

public class DataClient {
    // setup path of datafile
    private final static String pathDataFile = "";
    private String username=null;
    private String password=null;
    private  String id=null;
    private  boolean isLogin = false;

    public DataClient(String username, String password){
        this.username = username;
        this.password = password;
    }

    public DataClient(String id, String username, String email, String name, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isLogin = true;
    }

    private ArrayList<DataClient> getDataFromDataBase(){
        return null;
    }

    public boolean checkUserExist(){
        return true;
    }

    // Check info of curren client
    public boolean checkInfo(){
        return true;
    }

    // Check -> add data of client to Data
    public boolean addDataClient(){
        return true;
    }

    public boolean removeDataClient(){
        return true ;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getId(){
        return this.id;
    }

    public boolean equals(DataClient client){
        return this.username == client.username;
    }


}
