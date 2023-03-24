/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grandprix;

import java.util.ArrayList;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

/**
 *
 * @author Anja
 */
public class Championship  {
    
    private ArrayList<Driver> drivers = new ArrayList<Driver>();
    private ArrayList<Venue> venues = new ArrayList<Venue>(); 
    ArrayList<String> driverList = new ArrayList<String>();
    ArrayList<String> venueList = new ArrayList<String>();
       
    final int MINOR_MECHANICAL_FAULT = 5;
    final int MAJOR_MECHANICAL_FAULT = 3; 
    final int UNRECOVERABLE_MECHANICAL_FAULT = 1;
      
    String sP = System.getProperty("file.separator");
    String strCurrentLine; 
    
    File driver = new File("." + sP + "vozaci.txt");
    File venue = new File("." + sP + "staze.txt");
    
    
    public Championship(ArrayList<Driver> drivers, ArrayList<Venue> venues ) throws IOException{
   
        this.drivers = drivers; 
        this.venues = venues; 
        
        BufferedReader bufferedReaderDriver = new BufferedReader(new FileReader(driver));
        BufferedReader bufferedReaderVenue = new BufferedReader(new FileReader(venue));
        
        while ((strCurrentLine = bufferedReaderDriver.readLine()) != null) {
            driverList.add(strCurrentLine);
        }
        
        while ((strCurrentLine = bufferedReaderVenue.readLine()) != null) {
            venueList.add(strCurrentLine);
        }

        for(int i = 0; i < driverList.size(); i++){
            String[] splitDrivers = driverList.get(i).split("\\,");
            String name = splitDrivers[0];
            int ranking = Integer.parseInt(splitDrivers[1]);
            String specialSkill = splitDrivers[2];
            Driver d = new Driver(name, ranking, specialSkill);
            drivers.add(d);
        }
      
         for(int i = 0; i < venueList.size(); i++){
            String[] splitVenues= venueList.get(i).split("\\,");
            String venueName = splitVenues[0];
            int numberOfLaps = Integer.parseInt(splitVenues[1]);
            int averageLapTime = Integer.parseInt(splitVenues[2]);
            double chanceOfRain = Double.parseDouble(splitVenues[3]);
            Venue v = new Venue(venueName, numberOfLaps, averageLapTime, chanceOfRain);
            venues.add(v);
         }
    }
    
    public void prepareForTheRace(){
        
        int previousTime;
        
        for(int i = 0; i < drivers.size(); i++){
            if(drivers.get(i).isEligibleToRace() == false)
                drivers.remove(i);
        }
        
        int[] ranking = new int[drivers.size()];
        
        for( int i = 0; i < ranking.length; i++){
            ranking[i] = drivers.get(i).getRanking();
                 
            switch(ranking[i]){
                case 1:
                    previousTime = drivers.get(i).getAccumulatedTime();
                    drivers.get(i).setAccumulatedTime(previousTime + 0);
                    break;
                case 2:
                    previousTime = drivers.get(i).getAccumulatedTime();
                    drivers.get(i).setAccumulatedTime(previousTime + 3);
                    break; 
                case 3:
                    previousTime = drivers.get(i).getAccumulatedTime();
                    drivers.get(i).setAccumulatedTime(previousTime + 5);
                    break;
                case 4:
                    previousTime = drivers.get(i).getAccumulatedTime();
                    drivers.get(i).setAccumulatedTime(previousTime + 7);
                    break;
                default:
                    previousTime = drivers.get(i).getAccumulatedTime();
                    drivers.get(i).setAccumulatedTime(previousTime + 10);
                    break;
            }
        }
        
//        System.out.println();
//        
//        System.out.println("Akumulirana vremena vozaca na pocetku trke su:");
//        for(int i = 0; i < drivers.size(); i++){
//            System.out.println("Vozac " + drivers.get(i).getName() + "ima vreme od " + drivers.get(i).getAccumulatedTime() + " sekundi.");
//        }
//        
//        System.out.println();
//        
//        System.out.println("Akumulirani bodovi vozaca na pocetku trke su:");
//        for(int i = 0; i < drivers.size(); i++){
//            System.out.println("Vozac " + drivers.get(i).getName() + "ima " + drivers.get(i).getAccumulatedPoints() + " bodova.");
//        }
//        
//        System.out.println();
//        
//        System.out.println("Sposobnost vozaca da se trkaju u prestojecoj trci:");
//        for(int i = 0; i < drivers.size(); i++){
//            if(drivers.get(i).isEligibleToRace())
//                System.out.println("Vozac " + drivers.get(i).getName() + " je sposoban da se trka.");
//            else
//                System.out.println("Vozac " + drivers.get(i).getName() + " nije sposoban da se trka.");
//        }
        
        
    }
    
    public void driveAverageLapTime(int venueNumber){
        int seconds;
        int timeUntilNow;
        
        seconds = venues.get(venueNumber - 1).getAverageLapTime();
    
        for(int j = 0; j < drivers.size(); j++){
            timeUntilNow = drivers.get(j).getAccumulatedTime();
            drivers.get(j).setAccumulatedTime(seconds + timeUntilNow);
        }
        
//        System.out.println();
//        for(int m = 0; m < drivers.size(); m++){
//            System.out.println("Vreme vozaca " + drivers.get(m).getName() + " na osnovu prosecnog vremena trke je "  + drivers.get(m).getAccumulatedTime() + " sekundi.");
//        }
        
    }
    
    public void applySpecialSkills(int lapNumber){
        
        for(int i = 0; i < drivers.size(); i++){
            if(drivers.get(i).getSpecialSkill().equals("Cornering") || drivers.get(i).getSpecialSkill().equals("Braking")){
                    RNG rng1 = new RNG(1,8); 
                    drivers.get(i).useSpecialSkill(rng1);
            }
            else{
                if(lapNumber % 3 == 0){
                    RNG rng2 = new RNG(10,20);
                    drivers.get(i).useSpecialSkill(rng2);
                }
            }    
        }  
    }
    
    public void checkMechanicalProblem(){
     
        System.out.println();
        for(int i = 0; i < drivers.size(); i++){
            RNG rng = new RNG(1,101);
            int randomNumber = rng.getRandomValue();
            if (randomNumber > MINOR_MECHANICAL_FAULT && randomNumber <= 10 ){
                int addedTime = drivers.get(i).getAccumulatedTime() + 20;
                drivers.get(i).setAccumulatedTime(addedTime);
                System.out.println("Vozacu " + drivers.get(i).getName() + " se desio manji mehanicki kvar.");

            }
            else if (randomNumber >= MAJOR_MECHANICAL_FAULT && randomNumber <= MINOR_MECHANICAL_FAULT){
                int addedTime = drivers.get(i).getAccumulatedTime() + 120;
                drivers.get(i).setAccumulatedTime(addedTime);
                System.out.println("Vozacu " + drivers.get(i).getName() + " se desio ozbiljan mehanicki kvar.");
            }
            else if (randomNumber == 1){
                drivers.get(i).setEligibleToRace(false);
                System.out.println("Vozac " + drivers.get(i).getName() + " ce zbog kvara biti izbacen iz trke.");
            }
        }   
    }

    public void printLeader(int lap){
        
        int minAccumulatedTime = drivers.get(0).getAccumulatedTime();
        int driverNumber = 0;
        for(int i = 1; i < drivers.size(); i++){
            if(minAccumulatedTime > drivers.get(i).getAccumulatedTime()){
                minAccumulatedTime = drivers.get(i).getAccumulatedTime();
                driverNumber = i;
            }
                
        }
        
//        System.out.println();
//        for(int j = 0; j < drivers.size(); j++){
//            System.out.println("Akumulirano vreme vozaca " + drivers.get(j).getName() + " nakon ovog kruga je " + drivers.get(j).getAccumulatedTime());           
//        }
        
        System.out.print("Leader nakon " + lap + ". kruga je " + drivers.get(driverNumber).getName() + " sa prolaznim vremenom od " + drivers.get(driverNumber).getAccumulatedTime() + " sekundi.");
        
    }
    
    public void checkChancesOfRain(int numberOfLap, int venueNumber){
        
        double chanceOfRain = venues.get(venueNumber - 1).getChanceOfRain() * 100;
        RNG rng = new RNG(1,101);
        
        if(numberOfLap == 2){
            int randomNumber = rng.getRandomValue();
//            int randomNumber = 1;
            if ((double)randomNumber <= chanceOfRain){
                System.out.println("Pala je kisa.");
                rng.setMinimumValue(1);
                rng.setMaximumValue(3);
                for(int i = 0; i < drivers.size(); i++){
                    int randomValue = rng.getRandomValue();
                    if(randomValue == 1){
                        int previousTime = drivers.get(i).getAccumulatedTime();
                        drivers.get(i).setAccumulatedTime(previousTime + 10);
                        System.out.println();
                        System.out.println("Usled kise, vozac " + drivers.get(i).getName() + " je zamenio gume.");
                    }
                    else{
                        int previousTime = drivers.get(i).getAccumulatedTime();
                        drivers.get(i).setAccumulatedTime(previousTime + 5);
                        System.out.println();
                        System.out.println("Uprkos kisi, vozac " + drivers.get(i).getName() + " nije zamenio gume.");
                    }
                }
            }
            
        }
    }
    
    public void printWinnersAfterRace(int venueName){
        
        Collections.sort(drivers, new SortingAccumulatedTime(1)); //sortirani vozaci tako da prvi ima najmanje vreme
        
//        System.out.println("Nakon sortiranja:");
//        for(int j = 0; j < 4; j++){
//            System.out.println("Akumulirano vreme vozaca:" + j + "je" + drivers.get(j).getAccumulatedTime());
//        }
        
        for(int i = 0; i < drivers.size(); i++ ){
            drivers.get(i).setRanking(i + 1);
        }
            
        //8 5 3 1 
        
        int[] newPoints = {8, 5, 3, 1};
        int previousPoints;
        
        for(int i = 0; i < 4; i++){
            previousPoints = drivers.get(i).getAccumulatedPoints();
            drivers.get(i).setAccumulatedPoints(previousPoints + newPoints[i]);
        }
        
    }
    
    public void printChampion(int numOfRaces){
        //nakon svih odvozanih trka, onaj koji ima najmanje akumuliranih bodova se ispisuje(takodje ih sortirati)
        Collections.sort(drivers, new SortingAccumulatedPoints(-1));
        
        System.out.println();
        for(int j = 0; j < drivers.size(); j++){
                System.out.println("Akumulirani bodovi vozaca " + drivers.get(j).getName() + " su " + drivers.get(j).getAccumulatedPoints() + ".");            
        }
        
        System.out.println();
        System.out.println("Nakon " + numOfRaces + " odvozanih trka, sampion je " + drivers.get(0).getName() + ".");
        
    }
    
        
    public void setDrivers(ArrayList<Driver> drivers) {
        this.drivers = drivers;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }
}
