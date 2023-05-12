/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servertask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
public class ConnectedClients implements Runnable{
    
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<Students> studentList;
    private ArrayList<Subjects> subjectList;
    
    public ArrayList<Students> getStudentList() {
        return studentList;
    }
    
    public ConnectedClients(Socket socket, ArrayList<Students> studentList, ArrayList<Subjects> subjectList) throws IOException {
        this.socket = socket;
        this.subjectList = subjectList;
        this.studentList = studentList;

        try {
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    public void run(){
        try {
            this.pw.println("MATCH");
            while(true){
                String line = this.br.readLine();
                if(line != null){
                    String[] splitData= line.split(":");
                    switch(splitData[0]){
                        case "Student":
                            String studentInfo = splitData[1];
                            if(studentInfo.split(",").length != 6){
                                JOptionPane.showMessageDialog(null, "Wrong input");  
                            } else {
                                boolean isInUse = false;
                                for(int i = 0; i < studentList.size(); i++){
                                    if(studentInfo.split(",")[4].equals(studentList.get(i).getUsername())){
                                        isInUse = true;
                                        break;
                                    }
                                }
                                
                                if(isInUse == true){
                                    JOptionPane.showMessageDialog(null, "Username is already in use"); 
                                }else{
                                    Students student = new Students(studentInfo);
                                    if(student.checkJMBG() == true && student.checkIndex() == true){
                                        pw.println("Student is being added successfully.");
                                        studentList.add(student);
                                        String username = studentInfo.split(",")[4];
                                        String password = studentInfo.split(",")[5];
                                        String writeStudent = username + ":" + password + ":" + "student";
                                        String fileName = "users.txt";

                                        try {
                                            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                                            bw.newLine();
                                            bw.write(writeStudent);
                                            bw.newLine();
                                            bw.close();
                                            System.out.println("Student is added to users.txt");
                                        } catch (IOException e) {
                                            System.out.println("ERROR adding student to users.txt");
                                        }
                                    } else {
                                        pw.println("Invalid student data.");
                                    }
                                }
                            }
                            
                            break;
                        case "Subject":
                            if(splitData[1].split(";").length != 2){
                                JOptionPane.showMessageDialog(null, "Wrong input");  
                            } else {
                                String subjectName = splitData[1].split(";")[0];
                                boolean subjectExists = false;
                                for (Subjects subject : subjectList) {
                                    if (subjectName.equals(subject.getSubjectName())) {
                                        subjectExists = true;
                                        break;
                                    }
                                }
                                if (subjectExists) {
                                    JOptionPane.showMessageDialog(null, "Subject already exists.");
                                } else {
                                    Subjects subject = new Subjects(splitData[1]);
                                    if (subject.checkInput() && subject.checkMaximumPoints()) {
                                        subjectList.add(subject);
                                        pw.println("Subject is being added successfully.");
                                    } else {
                                        pw.println("Invalid subject data.");
                                    }
                                }
                            }
                            break;
                        case "Admin":
                            if(splitData[1].split(";").length != 2){
                                JOptionPane.showMessageDialog(null, "Wrong input");  
                            }else{
                                 String username = splitData[1].split(";")[0];
                                 String password = splitData[1].split(";")[1];
                                 String writeAdmin = username + ":" + password + ":" + "admin";
                                 String fileName = "users.txt";

                                    try {
                                        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                                        bw.newLine();
                                        bw.write(writeAdmin);
                                        bw.newLine();
                                        bw.close();
                                        System.out.println("Admin is added to users.txt");
                                        pw.println("Admin is being added successfully.");
                                    } catch (IOException e) {
                                        System.out.println("Error adding admin to users.txt");
                                    }
                            }
                            break;
                        case "Students and Subjects":
                            String message = "";
                            for(int i = 0; i < studentList.size(); i++){
                                //System.out.println(studentList.get(i).getIndex());
                                message = message + studentList.get(i).getIndex() + ";" ;
                                
                            }
                            message = message + ",";
                            
                            for(int i = 0; i < subjectList.size(); i++){
                                message = message + subjectList.get(i).getSubjectName() + ";" ;
                            }
                            pw.println("Studenti:");
                            pw.println(message);
                                                        
                            break;
                        case "Assigning":
                            String index = splitData[1];
                            String predmet = splitData[2];
                            boolean exist = false; 
                            for(int i = 0; i < studentList.size(); i++){
                                if(index.equals(studentList.get(i).getIndex())){
                                    for(int j = 0; j < studentList.get(i).getSubjects().size(); j++){
                                        if(predmet.equals(studentList.get(i).getSubjects().get(j).getSubjectName())){
                                            exist = true; 
                                        }
                                    }
                                    
                                    if(exist == false){
                                        for(int k = 0; k < subjectList.size(); k++){
                                            if(predmet.equals(subjectList.get(k).getSubjectName())){
                                                studentList.get(i).getSubjects().add(subjectList.get(k));
                                                break;
                                            }
                                        }
                                        JOptionPane.showMessageDialog(null, "Subject is successfully assigned to the student.");
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Subject is already in use.");
                                        exist = false;
                                    }
                                }
                            }
                            break;
                        case "Selected student":
                            String infoMessage = "";
                            for(int i = 0; i < studentList.size(); i++){
                                if(splitData[1].equals(studentList.get(i).getIndex())){
                                    for(int j = 0; j < studentList.get(i).getSubjects().size(); j++){
                                        infoMessage = infoMessage + studentList.get(i).getSubjects().get(j).getSubjectName() + ";";
                                    }
                                }
                            }
                            pw.println("Choose subject:");
                            pw.println(infoMessage);
                            
                            break;
                        case "Selected subject":
                            String st = splitData[1].split(";")[0];
                            String sub = splitData[1].split(";")[1];
                            String catMessage = "";
                            for(int i = 0; i < studentList.size(); i++){
                                if(st.equals(studentList.get(i).getIndex())){
                                    for(int j = 0; j < studentList.get(i).getSubjects().size(); j++){
                                        if(sub.equals(subjectList.get(j).getSubjectName())){
                                            for(int k = 0; k < subjectList.get(j).getCategories().size(); k++){
                                                catMessage = catMessage + subjectList.get(j).getCategories().get(k) + ";";
                                            }
                                        }
                                    }
                                }
                            }
                            
                            pw.println("Choose category:");
                            pw.println(catMessage);
                            break;
                        case "Adding points":
                            for(int i = 0; i < studentList.size(); i++){
                                if(splitData[1].split(";")[0].equals(studentList.get(i).getIndex())){
                                    for(int j = 0; j < studentList.get(i).getSubjects().size(); j++){
                                        if(splitData[1].split(";")[1].equals(studentList.get(i).getSubjects().get(j).getSubjectName())){
                                            if(studentList.get(i).getSubjects().get(j).addPoints(splitData[1].split(";")[2], splitData[1].split(";")[3]) == true ){
                                                pw.println("Points are added successfully.");
                                            }else{
                                                pw.println("Invalid points data.");
                                            }
                                        }
                                    }
                                }
                            }
                            
                            break;
                        case "See points":
                            String sendPoints = "";
                            for(int i = 0; i < studentList.size(); i++){
                                if(splitData[1].equals(studentList.get(i).getUsername())){
                                    sendPoints = studentList.get(i).printPoints();
                                }
                            }
                            pw.println("Points");
                            pw.println(sendPoints);
                            break;
                        case "See grades":
                            String sendGrades = "";
                            for(int i = 0; i < studentList.size(); i++){
                                if(splitData[1].equals(studentList.get(i).getUsername())){
                                    sendGrades = studentList.get(i).printGrades();
                                }
                            }
                            pw.println("Grades");
                            pw.println(sendGrades);
                            break;
                           
                        default:
                            JOptionPane.showMessageDialog(null, "ERROR.");
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Disconnected user.");
                    this.socket.close();
                    break;
                } 
                
            } 
        }catch (IOException ex) {
                System.out.println("Disconnected user.");
        }
    }
}
