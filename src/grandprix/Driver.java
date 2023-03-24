/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grandprix;

/**
 *
 * @author Anja
 */

public class Driver {
    
    private String name; 
    private int ranking; 
    private String specialSkill; 
    private boolean eligibleToRace = true;
    private int accumulatedTime; 
    private int accumulatedPoints; 

    public Driver(String name, int ranking, String specialSkill){
        this.name = name; 
        this.ranking = ranking; 
        this.specialSkill = specialSkill;
    }
    
    public void useSpecialSkill(RNG rng){
        int randomTime = rng.getRandomValue();
        if(randomTime == 0){
            System.out.println("RANDOM BROJ JE NULA");
        }
        int value = accumulatedTime - randomTime;
        this.accumulatedTime = value;
    }
       
    public void setName(String name) {
        this.name = name;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setSpecialSkill(String specialSkill) {
        this.specialSkill = specialSkill;
    }

    public void setEligibleToRace(boolean eligibleToRace) {
        this.eligibleToRace = eligibleToRace;
    }

    public void setAccumulatedTime(int accumulatedTime) {
        this.accumulatedTime = accumulatedTime;
    }

    public void setAccumulatedPoints(int accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
    }

    public String getName() {
        return name;
    }

    public int getRanking() {
        return ranking;
    }

    public String getSpecialSkill() {
        return specialSkill;
    }

    public boolean isEligibleToRace() {
        return eligibleToRace;
    }

    public int getAccumulatedTime() {
        return accumulatedTime;
    }

    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }
    
    //fale konstruktori po zelji
    //public void useSpecialSkills(RNG rng)
}
