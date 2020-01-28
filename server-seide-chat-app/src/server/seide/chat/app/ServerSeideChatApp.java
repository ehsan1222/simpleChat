/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.seide.chat.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.seide.chat.app.client.ClientHandler;
import server.seide.chat.app.file.FileManager;

/**
 *
 * @author ehsan
 */
public class ServerSeideChatApp {

    public static List<ClientHandler> members;
    
    
    /*
    incomming requests:
        
        login # id # password   -1) ok  -2) error # id or password is not currect -3)error # invalid request
    
        register # first_name # id # password # email      
    
    
    
    */
    
    
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        
        // get members from memory
        members = fileManager.getMembers();
        
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket;
            
            // client request
            while (true) {                
                
                // accept the incomming request
                socket = serverSocket.accept();
                
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                
                String requestMsg = dataInputStream.readUTF();
                
                ArrayList<String> parsedRequest = parseReqeust(requestMsg);
                
                switch(parsedRequest.get(0)) {
                    case "login":
                        if(parsedRequest.size() == 3) {
                            String id = parsedRequest.get(1);
                            String password = parsedRequest.get(2);
                            int memberIndex = isMemberExists(id);
                            
                            if(memberIndex >= 0 && members.get(memberIndex).getPassword().equals(password)) {
                                members.get(memberIndex).setDataInputStream(dataInputStream);
                                members.get(memberIndex).setDataOutputStream(dataOutputStream);
                                members.get(memberIndex).setS(socket);
                                members.get(memberIndex).setIsLoggedIn(true);
                                members.get(memberIndex).run();
                                
                                dataOutputStream.writeUTF("ok");
                                
                                // add this member to all logged in members
                                for(ClientHandler ch: members) {
                                    if ((!ch.getId().equals(id)) && (ch.getIsLoggedIn())) {
                                        String query = "loggedInMember # " + id + " # " + members.get(memberIndex).getFirstName();
                                        ch.getDataOutputStream().writeUTF(query);
                                        ch.getDataOutputStream().flush();
                                    }
                                }
                                
                            }else {
                                // id or password not currect
                                dataOutputStream.writeUTF("error # id or password is not currect");
                            }
                            
                        }else {
                            // invalid request
                            dataOutputStream.writeUTF("error # invalid request");
                        }
                         
                        break;
                        
                    case "register":
                        if (parsedRequest.size() == 5) {
                            // first_name # id # password # email
                            
                            String firstName = parsedRequest.get(1);
                            String id = parsedRequest.get(2);
                            String password = parsedRequest.get(3);
                            String email = parsedRequest.get(4);
                            
                            if (isMemberExists(id) < 0) {
                                ClientHandler clientHandler = new ClientHandler(firstName, id, password, email, dataInputStream, dataOutputStream, socket, Boolean.FALSE);
                                clientHandler.run();
                                members.add(clientHandler);
                                fileManager.saveMembers(members);
                                
                                dataOutputStream.writeUTF("ok");
                            } else {
                                // id is exists
                                dataOutputStream.writeUTF("error # id is exists");
                            }
                        } else {
                            // invalid request
                            dataOutputStream.writeUTF("error # invalid request");
                        }
                        break;
                }
                
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSeideChatApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }

    private static ArrayList<String> parseReqeust(String requestMsg) {
        ArrayList<String> parsedMessage = new ArrayList<>();
        
        Scanner stringParser = new Scanner(requestMsg);
        stringParser.useDelimiter("#");
        
        while (stringParser.hasNext()) {            
            parsedMessage.add(stringParser.next().trim());
        }
        
        return parsedMessage;
    }

    private static int isMemberExists(String id) {
        
        for(int i = 0; i< members.size(); i++){
            if (members.get(i).getId().equals(id)) {
                
                return i;
            }
        }
        return -1;
    }
    
    
}
