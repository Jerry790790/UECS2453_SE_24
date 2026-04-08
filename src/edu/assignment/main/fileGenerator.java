package edu.assignment.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Random;

public class fileGenerator{

    public static void generateFile(String filename) {
        Random rand = new Random();
        
        // Settings centered around your Warehouse (Kajang/Cheras)
        double startLat = 3.0100;
        double startLon = 101.7800;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // 1. Header
            writer.println("ID,Latitude,Longitude,PurchaseDate,DeadlineDate,Revenue,Penalty,Priority,Fragility,Capacity");

            for (int i = 1; i <= 1000; i++) {
                // ID
                int id = i;

                // Coordinates (±0.15 degree spread)
                double lat = startLat + (-0.15 + (0.15 - (-0.15)) * rand.nextDouble());
                double lon = startLon + (-0.15 + (0.15 - (-0.15)) * rand.nextDouble());

                // Dates (April 2026)
                LocalDate purchase = LocalDate.of(2026, 4, rand.nextInt(5) + 1);
                LocalDate deadline = purchase.plusDays(rand.nextInt(7) + 1);

                // Financials
                double revenue = 50 + (300 - 50) * rand.nextDouble();
                double penalty = 0.1 + (1.0 - 0.1) * rand.nextDouble(); // RM per minute

                // Logistics
                int priority = rand.nextInt(3) + 1; // 1, 2, or 3
                String[] fragileOptions = {"Normal", "Fragile", "High-Value"};
                String fragility = fragileOptions[rand.nextInt(fragileOptions.length)];
                
                // Capacity (Parcel weight in kg)
                double capacity = 5.0 + (95.0 * rand.nextDouble());

                // Write row
                writer.printf("%d,%.6f,%.6f,%s,%s,%.2f,%.2f,%d,%s,%.1f\n",
                        id, lat, lon, purchase, deadline, revenue, penalty, priority, fragility, capacity);
            }
            System.out.println("✅ Successfully generated: " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error generating file: " + e.getMessage());
        }
    }
}