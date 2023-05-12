/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servertask;

import java.util.ArrayList;
import java.io.Serializable;

/**
 *
 * @author Anja
 */
public class Subjects implements Serializable {

    private String subjectName;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> categoryName = new ArrayList<>();
    private ArrayList<Integer> minimumPoints = new ArrayList<>();
    private ArrayList<Integer> maximumPoints = new ArrayList<>();
    private int[] gainedPoints;
    private boolean isInteger = false;
    
    public String getSubjectName() {
        return subjectName;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<String> getCategoryName() {
        return categoryName;
    }
    
    public ArrayList<Integer> getMinimumPoints() {
        return minimumPoints;
    }

    public ArrayList<Integer> getMaximumPoints() {
        return maximumPoints;
    }
    
    public void printCategories(){
        for(String category : categories){
            System.out.println(category);
        }
    }
    
    public void printCategoryNames(){
        for(String name : categoryName){
            System.out.println(name);
        }
    }
    
    public void printMinimumPoints(){
        for(Integer min : minimumPoints){
            System.out.println(min);
        }
    }
    
    public void printMaximumPoints(){
        for(Integer max : maximumPoints){
            System.out.println(max);
        }
    }
    
    public int[] getGainedPoints() {
        return gainedPoints;
    }
    
    public Subjects(String subj){
        //stize format
        //ime;T1 25 22,T2 27 28
        //ime smestamo u subjectName
        this.subjectName = subj.split(";")[0];      
        //T1 25 22,T2 27 28 splitujemo po zarezu i smestamo u niz cat
        String[] cat = subj.split(";")[1].split(",");  
        //prolazimo kroz sve kategorije u nizu
        for (String cat1 : cat) {
            //svaku kategoriju dodajemo u listu categories
            this.categories.add(cat1);
            this.gainedPoints = new int[categories.size()];
            //kategorije splitujemo tako da dobijemo odvojeno T1 25 22
            String[] catData = cat1.split(" ");
            //prvi element je ime kategorije i smestamo ga u listu imena
            this.categoryName.add(catData[0]);
            try{
                //drugi element je minimalan broj poena ove kategorije
                this.minimumPoints.add(Integer.parseInt(catData[1]));
                //treci element je maksimalan broj poena ove kategorije
                this.maximumPoints.add(Integer.parseInt(catData[2]));
                //System.out.println("You entered an integer.");
                this.isInteger = true;
            }catch(NumberFormatException e ){
                this.isInteger = false;
                System.out.println("You did not entered an integer");
            }

        }
    }
    
    public boolean checkInput(){
        int counter = 0;
        for(String category : categories){
            String[] checkNumber = category.split(" ");
            
            if(checkNumber.length == 3){
                counter = counter + 1;
            }
        }
        
        if(counter != categories.size() || this.isInteger == false){
            System.out.println("Input in wrong format.");
            return false;
        }else{
            return true;
        }

    }
    
    public boolean checkMaximumPoints(){
        //suma maksimalnih poena ne sme biti veca od 100
        //minimum mora biti manji od maksimuma
        int sumOfPoints = 0;
        boolean correct = true;
        for(int i = 0; i < maximumPoints.size(); i++){
            sumOfPoints += this.maximumPoints.get(i);
            if(this.minimumPoints.get(i) >= this.maximumPoints.get(i)){
                correct = false;
                break;
            }
        }
        
        if(sumOfPoints == 100 && correct == true){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean addPoints(String category, String points){
        int pointsInt = Integer.parseInt(points);
        boolean added = false;
        for(int i = 0; i < this.categories.size(); i++){
            if(category.equals(this.categories.get(i))){
                if(pointsInt >= this.minimumPoints.get(i) && pointsInt <= this.maximumPoints.get(i)){
                    this.gainedPoints[i] = pointsInt;
                    added = true;
                }
            }
        } 
        
        return added;
    }
   
}
