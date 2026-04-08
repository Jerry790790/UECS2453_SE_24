package edu.assignment.algorithm;

import java.io.File;

import edu.assignment.dataStructure.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

/* Algorithm:  Shortest Process Next
 * 
 * To allow the delivery time to be short for each delivery, delivery will focus on the nearest address 
 * to deliver parcels until all parcels are delivered. Primary focus is distance	
 */

public class ShortestProcessNext {
	
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");



    public static void executeSPN(Scanner scanner, File dataFile) {
        List<DeliveryJob> allJobs = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        // 1. Load Data
        try (Scanner fileScanner = new Scanner(dataFile)) {
            if (fileScanner.hasNextLine()) fileScanner.nextLine(); // Skip CSV Header
            while (fileScanner.hasNextLine()) {
                String[] p = fileScanner.nextLine().split(",");
                
                 // ID,Latitude,Longitude,PurchaseDate,DeadlineDate,Revenue,Penalty,Priority,Fragility,Capacity
                 //  0,      1 ,        2,           3,           4,      5,      6,       7,        8,       9

                
                DateTimeFormatter csvFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate datePart = LocalDate.parse(p[4].trim(),csvFormatter);
                LocalDateTime deadlineDate = datePart.atStartOfDay();
                allJobs.add(new DeliveryJob(
                    Integer.parseInt(p[0].trim()),    // id
                    Double.parseDouble(p[1].trim()),  // lat
                    Double.parseDouble(p[2].trim()),  // lon
                    deadlineDate,					  // deadline
                    Double.parseDouble(p[5].trim()),  // revenue
                    Double.parseDouble(p[6].trim()),  // penaltypermin
                    Double.parseDouble(p[9].trim())  // capacity
                    


                    
                ));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // 2. Initialize KD-Tree
        KDTree<DeliveryJob> tree = new KDTree<>();
        tree.build(allJobs);

        // 3. Execution (Starting point near Kajang/Cheras)
        double curLat = 3.0100; 
        double curLon = 101.7800;
        double totalKM = 0;
        double totalPenalty = 0;
        double totalRevenue = 0;
        double currentTruckLoad = 0;
        int latecount = 0;
        
        LocalDateTime currentTime = DeliveryJob.startTime;
        
        System.out.println("\n========================================================");
        System.out.println("   KD-TREE NEAREST NEIGHBOR ROUTE (Units: Kilometers)   ");
        System.out.println("========================================================");

        while (visited.size() < allJobs.size()) {
            DeliveryJob next = tree.findNearest(curLat, curLon, visited);
            
            if (next == null) break;
            
            if ((currentTruckLoad + next.getParcelSize()) > DeliveryJob.truckSize) {
            	
            	double distanceToDepot = KDTree.calculateHaversine(curLat, curLon, DeliveryJob.START_LAT, DeliveryJob.START_LON);
            	totalKM += distanceToDepot;
            	double timeToDepot = (distanceToDepot/DeliveryJob.speedKMH) * 60;
            	currentTime = currentTime.plusMinutes((long)timeToDepot);
            	
            	curLat = DeliveryJob.START_LAT;
            	curLon = DeliveryJob.START_LON;
            	currentTruckLoad = 0;
            	
            	System.out.println("[EMPTY TRUCK] Returning to refill truck with parcels...");
            	
            	continue;
            }

            if (next != null) {
                double segmentKM = KDTree.calculateHaversine(curLat, curLon, next.getLat(), next.getLon());
                totalKM += segmentKM;
                
                // 1. Calculate Travel Time using java's time framework
                double travelTimeMins = segmentKM/DeliveryJob.speedKMH * 60;
                currentTime = currentTime.plusMinutes((long)travelTimeMins).plusMinutes((long)DeliveryJob.serviceTime);
                
                // 2. Add penalty if valid
                if (currentTime.isAfter(next.getDeadline())) {
                	latecount++; 
                	long minutesLate = Duration.between(next.getDeadline(), currentTime).toMinutes();
                	double penalty = minutesLate * next.getPenaltyPerMin();
                	totalPenalty += penalty;
                }
                
                // 3. setup for display
                
                totalRevenue = totalRevenue + next.getRevenue();
                
                curLat = next.getLat();
                curLon = next.getLon();
                visited.add(next.getId());
                String status = (currentTime.isAfter(next.getDeadline())) ? "[LATE]" : "[ON TIME]";

                System.out.printf("Stop %-2d | Job ID: %-3d | Distance: %6.2f km | Revenue: RM%7.2f | Date: %s  | %s\n" , 
                                  visited.size(), next.getId(), segmentKM, next.getRevenue(), currentTime.format(formatter), status);
            }
        }
        System.out.println("========================================================");
        System.out.printf("   FINAL ROUTE SUMMARY: \n");
        System.out.println("========================================================\n");
        System.out.printf("Total Deliveries Completed: %d\n",visited.size());
        System.out.printf("Total Late Penalties: %d\n",latecount);
        System.out.printf("Total Distance To Travel: %f\n",totalKM);
        DateTimeFormatter finishTimeFormat = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy | HH:mm");
        System.out.printf("Expected Finish Time: %s\n",currentTime.format(finishTimeFormat));
        System.out.printf("Total Revenue: RM%.2f - RM%.2f Penalties\n",totalRevenue,totalPenalty);
        System.out.printf("Fuel Expenses: RM%.2f\n",DeliveryJob.fuelCostPerKM*totalKM);
        System.out.printf("Net Profit: RM%.2f\n",totalRevenue - totalPenalty - DeliveryJob.fuelCostPerKM*totalKM);

    }
}