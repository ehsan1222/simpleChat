/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.seide.chat.app.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import server.seide.chat.app.client.ClientHandler;

public class FileManager {
    
    
    private String currentPath;
    private final String FILE_NAME;
    public FileManager() {
        this.FILE_NAME = "output.txt";
        try {
            currentPath = new File(".").getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public List<ClientHandler> getMembers() {
        
        try {
            if (new File(currentPath + File.separator + FILE_NAME).exists()) {
                FileInputStream fileInputStream =
                    new FileInputStream(currentPath + File.separator + FILE_NAME);
            
            ObjectInputStream inputStream = 
                    new ObjectInputStream(fileInputStream);
            
            return (List<ClientHandler>) inputStream.readObject();
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public void saveMembers(List<ClientHandler> members) {
        try {
            FileOutputStream fileOutputStream =
                    new FileOutputStream(currentPath + File.separator + FILE_NAME);
            
            ObjectOutputStream objectOutputStream = 
                    new ObjectOutputStream(fileOutputStream);
            
            objectOutputStream.writeObject(members);
            
            objectOutputStream.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
