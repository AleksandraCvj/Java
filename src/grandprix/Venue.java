/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grandprix;

/**
 *
 * @author Anja
 */
public class Venue {
    
    private String venueName;
    private int numberOfLaps;
    private int averageLapTime; 
    private double chanceOfRain; 
     
    
    public Venue(String venueName, int numberOfLaps, int averageLapTime, double chanceOfRain){
        this.venueName = venueName; 
        this.numberOfLaps = numberOfLaps; 
        this.averageLapTime = averageLapTime; 
        this.chanceOfRain = chanceOfRain; 
    }


    public void setAverageLapTime(int averageLapTime) {
        this.averageLapTime = averageLapTime;
    }

    public void setChanceOfRain(double chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public void setNumberOfLaps(int numberOfLaps) {
        this.numberOfLaps = numberOfLaps;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public int getAverageLapTime() {
        return averageLapTime;
    }

    public double getChanceOfRain() {
        return chanceOfRain;
    }

    public int getNumberOfLaps() {
        return numberOfLaps;
    }

    public String getVenueName() {
        return venueName;
    }
    
    //fale konstruktori po izboru
}
