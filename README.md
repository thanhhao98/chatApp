# chatApp 
This is an hybrid chat application (P2P and client-server combined) we wrote for our Computer Network assignment.
You can chat or send files with it :)


## 1/List protocal comunication between client and server
### *login:

            login username password
            
### *send msg:

            send username  this is message
            
            
### *recive msg:


            recv username    this is message
            
            
### *get list user active:  

            get listActive
            
            
### *logut: 

    
            logout
            
            
### *quit:  

                
            quit
            
            
### *register:  


            register newusername password
            
            
### *send file:             


            sendfile thanhhao /Users/mpxt2/bk/computerNetwork/Ass/demoFileTranfer/a.txt
            
            
### *recv file: 


            recvfile admin filename 2051 ( cmd - fromuser - sizefile *ignore size*)
            
            
## 2/Running description
### *Complie:
Go to /src/com/muc :

    javac *.java

(Should modify path of Data.xml in DataClient.java)
### *Server: 


    java ServerMain
    
    
### *ChatClient:


    java ConnectFrame
    
    

(You should use some ide such as netbean or intellij for support running)
