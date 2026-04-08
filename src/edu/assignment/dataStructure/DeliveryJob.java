package edu.assignment.dataStructure;
import java.time.LocalDateTime;

public class DeliveryJob implements Comparable<DeliveryJob>{
	
	// global variables
	// Pre-set Assumptions before the algorithm begins
	public static final double speedKMH = 35.0;
	public static final double fuelCostPerKM = 0.5;
	public static final double serviceTime = 5.0; // expected mins required to drop a parcel
	public static final double START_LAT = 3.0100; // warehouse latitude/longitude settings
	public static final double START_LON = 101.7800;
    public static final double truckSize = 1000; //1000kg
    public static final LocalDateTime startTime = LocalDateTime.of(2026,4,1,8,0); // start job at 1st april 2026 8am

    

	
	// instance variables
    // ID,Latitude,Longitude,PurchaseDate,DeadlineDate,Revenue,Penalty,Priority,Fragility,Capacity

    private int id;
    private double lat, lon;
    private LocalDateTime purchaseDate;
    private LocalDateTime deadline;
    private double revenue;
    private double penaltyPerMin;
    private int priority;
    private String fragility;
    private double parcelSize;
    

    // Create different constructors based on your favorability for your algorithm, or you put preset values using full consructor.
    public DeliveryJob(
    		int id, 
    		double lat, 
    		double lon,
    		LocalDateTime purchaseDate,
    		LocalDateTime deadline,
    		double revenue, 
    		double penaltyPerMin, 
    		int priority,
    		String fragility,
    		double parcelSize) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.purchaseDate = purchaseDate;
        this.deadline = deadline;
        this.revenue = revenue;
        this.penaltyPerMin = penaltyPerMin;
        this.priority = priority;
        this.fragility = fragility;
        this.parcelSize = parcelSize;
    }
    
    // constructor for ShortestProcessNext
    public DeliveryJob(
    		int id, 
    		double lat, 
    		double lon, 
    		LocalDateTime deadline, 
            double revenue, 
            double penalty, 
            double parcelSize
    		) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.deadline = deadline;
        this.revenue = revenue;
        this.penaltyPerMin = penalty;
        this.parcelSize = parcelSize;
    }
    
    public int getId() { return id; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public LocalDateTime getPutchaseDate() { return purchaseDate; }
    public LocalDateTime getDeadline() { return deadline; }
    public double getRevenue() { return revenue; }
    public double getPenaltyPerMin() { return penaltyPerMin; }
    public int getPriority() {return priority;}
    public String fragility() {return fragility;}
    public double getParcelSize() { return parcelSize; }

    
    
    @Override
    public int compareTo(DeliveryJob other) {
    	return Integer.compare(this.id, other.id);
    }

    
}