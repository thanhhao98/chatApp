package com.muc;

/*
List command
Id  detail  example
###Client to Server:
1       login                                       login username password
2       send msg                                    send username  this is message
3       recive msg                                  rev username    this is message
4       get list user active                        get listActive
5       logut                                       logout
6
*/

public class CmdMessage {
    private String command;
    private String[] tokens;
    private boolean validCommand;
    private Result result;

    public CmdMessage(String command){
        this.command = command;
        this.tokens = this.command.split("\\s");
        handleCommand();
    }

    private void handleCommand() {
        switch(tokens[0]){
            case "login":
                if(tokens.length == 3){
                    this.validCommand = true;
                    this.result = new Result(1);
                    this.result.setUsername(tokens[1]);
                    this.result.setPassword(tokens[2]);
                }
            case "send":
                //// send thanhhao Xin chao ban -> tokens ['send','thanhhao','Xin','chao','ban'] -> fix case nay.
                this.validCommand = true;
                this.result = new Result(2);
                this.result.setUsername(tokens[1]);
                this.result.setMessage(tokens[2]);
        }
    }

}

class Result {
    private final int type;
    private String username;
    private String password;
    private String message;

    public Result(int type){
        this.type = type;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
