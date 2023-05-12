/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clients.rszeos;

import java.io.BufferedReader;
import java.io.IOException;
import static java.lang.constant.ConstantDescs.NULL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Anja
 */
public class ReceiveMessageFromServer implements Runnable {
    
    ClientDesign parent;
    BufferedReader br;
    
    public ReceiveMessageFromServer(ClientDesign parent){
        this.parent = parent;
        this.br = this.parent.getBr();
    }
    
    @Override
    public void run(){
        try{
            String line;
            while(true){
                line = this.br.readLine();
                if(line != NULL){
                    switch(line){
                        case "MATCH":
                            this.parent.getTfUsername().setEnabled(false);
                            this.parent.getTfPassword().setEnabled(false);
                            this.parent.getCbRole().setEnabled(false);
                            this.parent.getCbHide().setEnabled(false);
                            this.parent.getBtnLogin().setEnabled(false);
                            this.parent.getjLabel1().setEnabled(false);
                            this.parent.getjLabel2().setEnabled(false);
                            this.parent.getjLabel3().setEnabled(false);
                            if(this.parent.getCbRole().getSelectedItem().equals("admin")){
                                this.parent.getjLabel5().setEnabled(true);
                                this.parent.getCbAdminTasks().setEnabled(true);
                            }else{
                                this.parent.getjLabel10().setEnabled(true);
                                this.parent.getBtnSeeGrades().setEnabled(true);
                                this.parent.getBtnSeePoints().setEnabled(true);
                                this.parent.getTaGrades().setEnabled(true);
                                this.parent.getTaPoints().setEnabled(true);
                            }
                            break;
                        case "Student is being added successfully.":
                            this.parent.getTfData().setText("");
                            this.parent.getTfData().setEnabled(false);
                            this.parent.getBtnOK().setEnabled(false);
                            this.parent.getCbAdminTasks().setSelectedIndex(0);
                            this.parent.getjLabel6().setText("");
                            JOptionPane.showMessageDialog(null, "Student is being added successfully.");
                            break;
                        case "Invalid student data.":
                            JOptionPane.showMessageDialog(null, "Invalid student data.");
                            break;
                        case "Subject is being added successfully.":
                            this.parent.getTfData().setText("");
                            this.parent.getTfData().setEnabled(false);
                            this.parent.getBtnOK().setEnabled(false);
                            this.parent.getCbAdminTasks().setSelectedIndex(0);
                            this.parent.getjLabel6().setText("");
                            JOptionPane.showMessageDialog(null, "Subject is being added successfully.");
                            break;
                        case "Invalid subject data.":
                            JOptionPane.showMessageDialog(null, "Invalid subject data.");
                            break;
                        case "Studenti:":
                            line = this.br.readLine();
                            String[] splitData = line.split(",");
                            String[] index = splitData[0].split(";");
                            String[] subject = splitData[1].split(";");
                            this.parent.getCbStudents().removeAllItems();
                            this.parent.getCbSubjects().removeAllItems();
                            for(int i = 0; i < index.length; i ++){
                                this.parent.getCbStudents().addItem(index[i]);
                            }

                            for(int i = 0; i < subject.length; i ++){
                                this.parent.getCbSubjects().addItem(subject[i]);
                            }

                            break;
                        case "Admin is being added successfully.":
                            this.parent.getTfData().setText("");
                            this.parent.getTfData().setEnabled(false);
                            this.parent.getBtnOK().setEnabled(false);
                            this.parent.getCbAdminTasks().setSelectedIndex(0);
                            this.parent.getjLabel6().setText("");
                            JOptionPane.showMessageDialog(null, "Admin is being added successfully.");
                            break;

                        case "Choose subject:":
                            line = this.br.readLine();
                            if(line.equals("")){
                                JOptionPane.showMessageDialog(null, "Selected student does not havy any subject assigned.");
                            }else{
                                String[] chooseSubject = line.split(";");
                                this.parent.getCbChooseSubject().removeAllItems();
                                for(int i = 0; i < chooseSubject.length; i ++){
                                this.parent.getCbChooseSubject().addItem(chooseSubject[i]);
                                }
                            }  
                            break;

                        case "Choose category:":
                            line = this.br.readLine();
                            String[] chooseCategory = line.split(";");
                            this.parent.getCbChooseCategory().removeAllItems();
                            for(int i = 0; i < chooseCategory.length; i ++){
                                this.parent.getCbChooseCategory().addItem(chooseCategory[i]);
                            }
                            break;
                        case "Points are added successfully.":
                            JOptionPane.showMessageDialog(null, "Points are added successfully.");
                            this.parent.getTfPoints().setText("");
                            break;
                        case "Invalid points data.":
                            JOptionPane.showMessageDialog(null, "Invalid points data.");
                            break;
                        case "Points":
                            line = this.br.readLine();
                            if(line.equals("")){
                                JOptionPane.showMessageDialog(null, "No points assigned.");
                            }else{
                                String[] points = line.split(";");
                                this.parent.getTaPoints().setText("");
                                for(int i = 0; i < points.length; i++){
                                    this.parent.getTaPoints().append(points[i]);
                                    this.parent.getTaPoints().append("\n");
                                }
                            }
                            
                            break;
                        case "Grades":
                            line = this.br.readLine();
                            if(line.equals("")){
                                JOptionPane.showMessageDialog(null, "No grades assigned.");
                            }else{
                                String[] grades = line.split(";");
                                this.parent.getTaGrades().setText("");
                                for(int i = 0; i < grades.length; i++){
                                    this.parent.getTaGrades().append(grades[i]);
                                    this.parent.getTaGrades().append("\n");
                                }
                            }
                            
                            break;
                    }
                }
            }
        } catch (IOException ex) {
                Logger.getLogger(ReceiveMessageFromServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
