/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servertask;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Anja
 */
class ShutingDownThread implements Runnable {
    
    private static ArrayList<Students> studentList = new ArrayList<>();
    private static ArrayList<Subjects> subjectList = new ArrayList<>();
    
    ShutingDownThread(ArrayList<Students> studentList, ArrayList<Subjects> subjectList){
        this.studentList = studentList;
        this.subjectList = subjectList;
        
    }
     
    public void run() {
         
        Scanner sc = new Scanner(System.in);
        while(true){
            String line = sc.nextLine();
            if(line.equals("shutdown")){
                FileOutputStream fout1 = null;
                try {
                    fout1 = new FileOutputStream("students.txt");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                ObjectOutputStream out1 = null;
                try {
                    out1 = new ObjectOutputStream(fout1);
                } catch (IOException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    out1.writeObject(studentList);
                    out1.flush();
                    out1.close();
                } catch (IOException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                FileOutputStream fout2 = null;
                try {
                    fout2 = new FileOutputStream("subjects.txt");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                ObjectOutputStream out2 = null;
                try {
                    out2 = new ObjectOutputStream(fout2);
                } catch (IOException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    out2.writeObject(subjectList);
                    out2.flush();
                    out2.close();
                } catch (IOException ex) {
                    Logger.getLogger(ShutingDownThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.exit(0);
            }
        }
    }
}

public class ServerTask {
    
    private ServerSocket serverSocket;
    private int port;
    BufferedReader br;
    private static ArrayList<Students> studentList = new ArrayList<>();
    private static ArrayList<Subjects> subjectList = new ArrayList<>();
    
    public void acceptClients() throws FileNotFoundException, IOException{
        Socket client = null;
        Thread thr;
        String identifikacija;
        boolean match = false;
        while (true) {
            try {
                System.out.println("Waiting for the clients...");
                client = this.serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerTask.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (client != null) {
                match = false;
                String sP = System.getProperty("file.separator");
                File users = new File("." + sP + "users.txt");
                String strCurrentLine; 

                ArrayList<String> userList = new ArrayList<>();

                BufferedReader readUsersFromFile = new BufferedReader(new FileReader(users));

                while ((strCurrentLine = readUsersFromFile.readLine()) != null) {
                    userList.add(strCurrentLine);
                }
                //if new client is connected, check if his info are in users.txt
                this.br = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                identifikacija = this.br.readLine();
                for(int i = 0; i < userList.size(); i++){
                    if(userList.get(i).equals(identifikacija)){
                        System.out.println("MATCH");
                        match = true;
                        //creating thread for every matched client
                        ConnectedClients clnt = new ConnectedClients(client, studentList, subjectList);
                        thr = new Thread(clnt);
                        thr.start();
                        break;
                    }
                }
                
                if(match == false){
                    JOptionPane.showMessageDialog(null, "Invalid useraname or password.");
                }
            }else{
                break;
        }
        }
    }
    

    public ServerTask(int port){
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ServerTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        ServerTask server = new ServerTask(6002);
        System.out.println("Server connected and waiting for new clients");
        

        try {
            File file1 = new File("students.txt");
            
            try (ObjectInputStream in1 = new ObjectInputStream(new FileInputStream(file1))) {
                if(file1.length() == 0){
                    System.out.println("File of students is empty");
                }else{
                    studentList = (ArrayList<Students>) in1.readObject();
                    System.out.println("File of students is not empty");
                }
            }
        } catch (EOFException e) {
            System.out.println("File of students is empty.");
        } catch (IOException | ClassNotFoundException e) {
        }
        
        
        try {
            File file2 = new File("subjects.txt");
            
            try (ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(file2))) {
                if(file2.length() == 0){
                    System.out.println("File of subjects is empty");
                }else{
                    subjectList = (ArrayList<Subjects>) in2.readObject();
                    System.out.println("File of subjects is not empty");
                }
            }
        } catch (EOFException e) {
            System.out.println("File of subjects is empty.");
        } catch (IOException | ClassNotFoundException e) {
        }
        
        FileWriter fw = new FileWriter("students.txt");
        fw.write("");
        fw.close();
        
        FileWriter fw1 = new FileWriter("subjects.txt");
        fw1.write("");
        fw1.close();
        
        
        ShutingDownThread sht = new ShutingDownThread(studentList,subjectList);
        Thread th = new Thread(sht);
        th.start();
        
        server.acceptClients();
        
    }

}
