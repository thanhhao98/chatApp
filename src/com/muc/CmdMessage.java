package com.muc;

// Not be used!


/*

Tắt session khi chưa logout -> đăng nhập lại lỗi. //handle tự logout, quit trên ui rồi 
đang login có thằng khác login

Gửi tn đến user đã tắt session khi chưa logout -> lỗi!


*/


/*
List command
Id  detail  example
###Client to Server:
1       login                                       login username password
2       send msg                                    send username  this is message
3       recive msg                                  recv username    this is message
4       get list user active                        get listActive
5       logut                                       logout
6       quit                                        quit
7       register                                    register newusername password
8       send file                                   sendfile thanhhao /Users/mpxt2/bk/computerNetwork/Ass/demoFileTranfer/a.txt
9       recv file                                   recvfile admin 2051 ( cmd - fromuser - sizefile *ignore size*)
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

    public void makeType1(String username, String password){
        this.result = new Result(1);
        this.result.setUsername(username);
        this.result.setPassword(password);
        this.validCommand = true;
    }

    public void makeType2(String username, String message){
        return ;
    }

    private void handleCommand() {
        switch(tokens[0]){
            case "login":
                if(tokens.length == 3) {
                    this.validCommand = true;
                    this.result = new Result(1);
                    this.result.setUsername(tokens[1]);
                    this.result.setPassword(tokens[2]);
                } else {
                    this.validCommand = false;
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
