/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package androidserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Anja
 */
public class AndroidConnectedClients implements Runnable {
    
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private String username;
    private String player1;
    private String player2;
    private boolean available = true;
    private ArrayList<AndroidConnectedClients> allClients;
    private boolean playAgain = false;
    int countGame = 1;

    public int getCountGame() {
        return countGame;
    }

    public void setCountGame(int countGame) {
        this.countGame = countGame;
    }

    public void setPlayAgain(boolean playAgain) {
        this.playAgain = playAgain;
    }

    public boolean isPlayAgain() {
        return playAgain;
    }
    
    public String getUsername() {
        return username;
    }
    
   public void setPlayer1(String player1) {
       this.player1 = player1;
    }

   public void setPlayer2(String player2) {
        this.player2 = player2;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public boolean isAvailable() {
        return available;
    }
    public AndroidConnectedClients(Socket socket, ArrayList<AndroidConnectedClients> allClients){
        this.socket = socket;
        this.allClients = allClients;
        this.available = true;
        this.player1 = null;
        this.player2 = null;
        try {
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
            this.username = "";
        } catch (IOException ex) {
            Logger.getLogger(AndroidConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void updateConnectedPlayers(){
        String connectedPlayers = "Players:";
        for (AndroidConnectedClients client : this.allClients) {
            if(client.isAvailable()){
                connectedPlayers += " " + client.getUsername();
            }
        }

        for (AndroidConnectedClients updateClient : this.allClients) {
                updateClient.pw.println(connectedPlayers);
        }

        System.out.println(connectedPlayers);
    }
    
    @Override
    @SuppressWarnings("empty-statement")
    public void run(){
        System.out.println("KLIJENT TU I USAO SI U RUN METODU");
        int i = 0;
        try {
            while(true){
                if(this.username.equals("")){
                    this.username = this.br.readLine();
                    if(this.username != null){
                        System.out.println("Connected user:" + this.username);
                        updateConnectedPlayers();
                    }
                }else{
                    String line = "";
                    System.out.println("Waiting for messages...");
                    try{
                        line = this.br.readLine();
                    }catch (IOException ex){
                        System.out.println("No messages" + ex);
                    }
                    if(!line.equals("")){
                        System.out.println(line);
                        switch(line.split(":")[0]){
                            case "Players":
                                //Players:onaj koji salje zahtev, onaj koji prima zahtev.
                                for(AndroidConnectedClients client:this.allClients){
                                    client.setPlayer1(line.split(":")[1].split(",")[0]);
                                    client.setPlayer2(line.split(":")[1].split(",")[1]);
                                }
                                
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.getUsername().equals(this.player2)){
                                            client.pw.println("Play game with:" + this.player1);
                                    }
                                }
                              break;
                            case "NO": 
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.username.equals(this.player1)){
                                       client.pw.println("Not accepted by:" + this.player2);
                                    }
                                }
                                break;
                            case "YES":
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.username.equals(this.player1)){
                                        client.pw.println("Accepted by:" + this.player2);
                                    }

                                }
                                
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.username.equals(this.player2)){
                                        client.pw.println("Set flag:");
                                    }

                                }
                                //dodavanje u liste i setovanje polja available na false
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.username.equals(this.player1)){
                                        client.setAvailable(false);
                                    }
                                }
                                
                                for(AndroidConnectedClients client:this.allClients){
                                    if(client.username.equals(this.player2)){
                                        client.setAvailable(false);
                                    }
                                }
                                break;
                            case "Move done":
                                if(line.split(":")[1].split(",")[0].equals("Player1")){
                                    for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player2)){
                                            client.pw.println("Change photo:" + line.split(":")[1]);
                                        }
                                    }
                                }else{
                                    for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player1)){
                                            client.pw.println("Change photo:" + line.split(":")[1]);
                                        }
                                    } 
                                }
                                break;
                            case "Winner":
                                System.out.println("WINNER IS " + line.split(":")[1]);
                                String winner;
                                if(line.split(":")[1].equals("Player1")){
                                    winner = this.player1;
//                                    System.out.println("Pobednik je:" + this.player1);
                                }else{
                                    winner = this.player2;
//                                    System.out.println("Pobednik je" + this.player2);
                                }
                                for(AndroidConnectedClients client:this.allClients){
                                    client.pw.println("Game done:" + winner);
                                }
                                break;
                            case "Yes from":
                                if(line.split(":")[1].equals("Player1")){
                                    for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player1)){
                                            client.setPlayAgain(true);
                                        }
                                    } 
                                }else{
                                    for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player2)){
                                            client.setPlayAgain(true);
                                        }   
                                    } 
                                }
                                
                                for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player1)){
                                            System.out.println("HOCE:" + client.isPlayAgain());
                                        }
                                } 
                                
                                for(AndroidConnectedClients client:this.allClients){
                                        if(client.getUsername().equals(this.player2)){
                                            System.out.println("HOCE:" + client.isPlayAgain());
                                        }
                                }
                                
                                for(AndroidConnectedClients client1:this.allClients){
                                    if(client1.getUsername().equals(this.player1) && client1.isPlayAgain() == true){
                                        for(AndroidConnectedClients client2:this.allClients){
                                            if(client2.getUsername().equals(this.player2) && client2.isPlayAgain() == true){
                                                client1.setCountGame(countGame + 1);
                                                client2.setCountGame(countGame + 1);
                                                System.out.println("IGRA TREBA PONOVO DA POCNE");
                                                if(this.countGame % 2 == 0){
                                                    client1.pw.println("Play again:2");
                                                    client2.pw.println("Play again:1");
                                                }else{
                                                    client1.pw.println("Play again:1");
                                                    client2.pw.println("Play again:2");
                                                }
                                                
                                                client1.setPlayAgain(false);
                                                client2.setPlayAgain(false);
                                                break;
                                                
                                            }   
                                        }
                                    }
                                }
                                break;
                            case "No from":
                                for(AndroidConnectedClients client:allClients){
                                    if(client.getUsername().equals(this.player1)){
                                        client.pw.println("Back to main menu:main");
                                        client.setCountGame(1);
                                        client.setAvailable(true);
                                    }
                                }
                                for(AndroidConnectedClients client:allClients){
                                    if(client.getUsername().equals(this.player2)){
                                        client.pw.println("Back to main menu:main");
                                        client.setCountGame(1);
                                        client.setAvailable(true);
                                    }
                                }
                                break;
                            default:
                                System.out.println("DEFAULTTT");
                                break;
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Disconnected user else.");
                        this.socket.close();
                        break;
                    } 
                }

            }
        }catch (IOException ex) {
                System.out.println("Disconnected user kec.");
        }
        
    }
}
