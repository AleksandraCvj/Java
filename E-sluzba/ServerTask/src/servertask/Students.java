/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servertask;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author Anja
 */
public class Students implements Serializable {
    
    private String name;
    private String surname;
    private String index;
    private String jmbg;
    private String username; 
    private String pass;
    private ArrayList<Subjects> subjects;

    public ArrayList<Subjects> getSubjects() {
        return subjects;
    }
    
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getIndex() {
        return index;
    }

    public String getJmbg() {
        return jmbg;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }
    
    public Students(String studentData){
        this.subjects = new ArrayList<>();
        String[] studentInfo = studentData.split(",");
        this.name = studentInfo[0];
        this.surname = studentInfo[1];
        this.index = studentInfo[2];
        this.jmbg = studentInfo[3];
        this.username = studentInfo[4];
        this.pass = studentInfo[5];
    }
    
    public boolean checkJMBG(){
        if(this.jmbg.length() != 13){
            System.out.println("Wrong number of digits");
            return false;
        } else {
            String jmbgDate = this.jmbg.substring(0,7);
            //System.out.println(jmbgDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyy");
            //ako je true, onda prilikom unosa 3002998, moze da napravi izmenu i smatra ga validnim datumom
            dateFormat.setLenient(false);
            //hvata exception u slucaju da taj datum stvarno ne postoji
            try{
                //parsiranje datuma
                dateFormat.parse(jmbgDate);
                //System.out.println("Valid date:" + date);
            }catch (ParseException e){
                System.out.println("Invalid date format");
                return false;
            }
            
            //System.out.println("Right number of digits");
            return true;
        } 
    } 
    
    public boolean checkIndex(){
        String regexPattern = "[Ee][123][/-](20(0[0-9]|1[0-9]|2[0-2]|23))";
        if(this.index.matches(regexPattern)){
            return true;
        }
        else{
            System.out.println("Wrong index value.");
        }
        return false;
    }
    
    public String printPoints(){
        String points = "";
        String categories = "";
        for(int i = 0;  i < subjects.size(); i++){
            for(int j = 0; j < subjects.get(i).getCategoryName().size(); j++){
                categories = categories + subjects.get(i).getCategoryName().get(j) + " " + subjects.get(i).getGainedPoints()[j] + ",";
                
            }
            points = points + subjects.get(i).getSubjectName() + ":" + categories + ";";
            categories = "";
            
        }
        return points;
    }
    
    public String printGrades(){
        String grades = "";
        String grade = "";
        int sumOfPoints = 0;
        for(int i = 0;  i < subjects.size(); i++){
            for(int j = 0; j < subjects.get(i).getCategoryName().size(); j++){
                sumOfPoints = sumOfPoints + subjects.get(i).getGainedPoints()[j];
            }
            
            if(sumOfPoints < 51){
                grade = "Failed the exam";
            }else if(sumOfPoints >= 51 && sumOfPoints < 61){
                grade = "6";
            }else if(sumOfPoints >= 61 && sumOfPoints < 71){
                grade = "7";
            }else if(sumOfPoints >= 71 && sumOfPoints < 81){
                grade = "8";
            }else if(sumOfPoints >= 81 && sumOfPoints < 91){
                grade = "9";
            }else{
                grade = "10";
            }
            
            grades = grades + subjects.get(i).getSubjectName() + ":" + grade + ";";
            sumOfPoints = 0;
            grade = "";
        }
        
        return grades; 
    }
    
}
