/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.seide.chat.app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author ehsan
 */
public class ClientHandler implements Runnable, Serializable{
    
    private String firstName;
    private String id;
    private String password;
    private String email;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket s;
    private Boolean isLoggedIn;

    public ClientHandler(String firstName, String id, String password, String email, DataInputStream dataInputStream, DataOutputStream dataOutputStream, Socket s, Boolean isLoggedIn) {
        this.firstName = firstName;
        this.id = id;
        this.password = password;
        this.email = email;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.s = s;
        this.isLoggedIn = isLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    /*
    api ----->
        get-> logout    send-> ok
        get-> message # client_id # textMessage
    */
    @Override
    public void run() {
        
        String recivedMessage;
        
        while (true) {            
            try {
                recivedMessage = dataInputStream.readUTF();
                
                if (recivedMessage.equals("logout")) {
                    this.setIsLoggedIn(false);
                    this.dataOutputStream.writeUTF("ok");
                    this.s.close();
                    break;
                }
                
                
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
    }
    
}
