/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package grandprix;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Anja
 */
public class Simulate {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        int numberOfRaces;
        int venueNumber;
        
        ArrayList<Driver> drivers = new ArrayList<>();
        ArrayList<Venue> venues = new ArrayList<>();
        
        Scanner sc = new Scanner(System.in);
        
        do{
            System.out.println("Koliko ce se trka voziti u trenutnom sampionatu? Moguci broj trka je 3-5.");
            numberOfRaces = sc.nextInt();
        }while(numberOfRaces < 3 || numberOfRaces > 5);

        Championship sampion = new Championship(drivers, venues);
             
        for(int i = 1; i <= numberOfRaces; i++){
            System.out.println();
            System.out.println();
            
            do{
                System.out.println("Izaberi jednu od ponudjenih trka:");

                for(int j = 0; j < venues.size(); j++){
                    System.out.println((j + 1) + "." + venues.get(j).getVenueName());
                }
                
                venueNumber = sc.nextInt();
            }while(venueNumber > venues.size());
            
            String venueName = venues.get(venueNumber - 1).getVenueName();    
            System.out.println("Odabrana je trka " + venueName + ".");

            sampion.prepareForTheRace();

            int lapNumber = venues.get(venueNumber - 1).getNumberOfLaps();
            for(int k = 1; k <= lapNumber; k++){
                sampion.driveAverageLapTime(venueNumber);
                sampion.applySpecialSkills(k);
                sampion.checkMechanicalProblem();
                sampion.printLeader(k);
                sampion.checkChancesOfRain(k, venueNumber);
            
            }
            
//            System.out.println();
//            for(int j = 0; j < drivers.size(); j++){
//                System.out.println("Akumulirano vreme vozaca " + drivers.get(j).getName() + " je " + drivers.get(j).getAccumulatedTime());            
//            }
            
            sampion.printWinnersAfterRace(venueNumber);
            
            System.out.println();
            System.out.println(); 
            System.out.println("Renking:");
            for(int k = 0; k < 4; k++){
                System.out.println((k + 1) + "." + drivers.get(k).getName() + ", vreme: " + drivers.get(k).getAccumulatedTime() + "s");
            }
            
//            System.out.println();
//            for(int j = 0; j < drivers.size(); j++){
//                System.out.println("Akumulirani bodovi vozaca " + drivers.get(j).getName() + " su " + drivers.get(j).getAccumulatedPoints());            
//            }
            
            venues.remove(venueNumber - 1);
        }
        
        sampion.printChampion(numberOfRaces);

    }
    
}
