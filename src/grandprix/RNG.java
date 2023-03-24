/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grandprix;

import java.util.Random;

/**
 *
 * @author Anja
 */
public class RNG {
    
    private int minimumValue; 
    private int maximumValue; 
    private Random rnd = new Random();
    
    public RNG(int minValue, int maxValue){
        this.minimumValue = minValue;
        this.maximumValue = maxValue;
    }
    
    int getRandomValue(){
        
        //stigne nam 1 i 8 
        int randomValue = rnd.nextInt(maximumValue - minimumValue) + minimumValue;
        
        return randomValue;
    }

    public void setMinimumValue(int minimumValue) {
        this.minimumValue = minimumValue;
    }

    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    public int getMinimumValue() {
        return minimumValue;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public Random getRnd() {
        return rnd;
    }

    
}
