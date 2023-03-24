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
public class SortingAccumulatedTime implements Comparator<Driver> {
    
    int direction = 1;
    
    public SortingAccumulatedTime(int direction){
        if(direction != 1 && direction != -1){
            direction = 1;
        }
        this.direction = direction;
    }
    
    @Override
    public int compare(Driver d1, Driver d2)
    {
        return (d1.getAccumulatedTime() - d2.getAccumulatedTime()) * direction;
    }
}
