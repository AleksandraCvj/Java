/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grandprix;

import java.util.Comparator;

/**
 *
 * @author Anja
 */
public class SortingAccumulatedPoints implements Comparator<Driver> {

    int direction = 1;
    
    public SortingAccumulatedPoints(int direction){
        if(direction != 1 && direction != -1){
            direction = 1;
        }
        this.direction = direction;
    }
    
    @Override
    public int compare(Driver d1, Driver d2)
    {
        return (d1.getAccumulatedPoints() - d2.getAccumulatedPoints()) * direction;
    }
} 
