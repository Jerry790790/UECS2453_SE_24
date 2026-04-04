package edu.assignment.algorithm;

// Import used frameworks for the Main program here
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;


// Import the algorithm class here

public class Main {

	
	
	public static void main (String[] args) {
		
		// Initialization of variables
		Scanner scanner = new Scanner(System.in);
		
		boolean main = true;
		while(main == true) {
			System.out.println("Welcome to Job Sequencing Algorithm Analysis Program");
			System.out.println("Case Study: Logistics & Delivery Scheduling");
			System.out.println("The data input for this case study is as the following:");
			System.out.println("1. Purchase date");
			System.out.println("2. Deadline date");
			System.out.println("3. Delivery Time");
			System.out.println("4. Penalty cost");
			System.out.println("5. Priority Level");
			System.out.println("6. Product Fragility");
			System.out.println("7. Coordinates in Latitude/Longitude");
			
			System.out.println("========================================================");
			System.out.println("Enter your instructions:");
			System.out.println("(1) Read data input from a file");
			System.out.println("(2) Random data generation");
			System.out.println("(Q) Quit program");
					
			switch (scanner.nextLine().toUpperCase()) {
			case "1": {
				chooseAlgorithm(scanner,readFiles(scanner));
				//main = false;
				//read data
				break;
			}
			case "2": {
				System.out.println("random gen");
				//random generation
				break;
			}
			case "Q": {
				System.out.println("Exitting program...");
				scanner.close();
				main = false;
				break;
			}
			default: {
				System.out.println("Invalid choice");
			}
			
			}
		}	
	}
	/* This function lists out all the available datas in edu/assignment/data and prompts the 
	 * user to select a data they want to use for the algorithm. 
	 * 
	 * It will return the data as a File object, so it will be passed into the algorithm classes. */
	public static File readFiles(Scanner scanner) {
		File folder = new File("src/edu/assignment/data");
	    // Check if the directory exists
	    if (!folder.exists() || !folder.isDirectory()) {
	    	System.out.println("The path edu/assignment/main is not found");
	    	return null;
	    }
	        File[] listOfFiles = folder.listFiles();

	        if (listOfFiles.length == 0) {
	        	System.out.println("No Data Files Available");
	        	scanner.nextLine();
	        	return null;
	        }
	        
	        boolean readingFile = true;
	        while (readingFile == true) {
		        System.out.println("Available Data Files:");
		        for (int i = 0; i < listOfFiles.length; i++) {
		            if (listOfFiles[i].isFile()) {
		                System.out.println("[" + (i + 1) + "] " + listOfFiles[i].getName());
		            }
		        }
		        System.out.println("Enter the file you want to read in numerical order: ");
		        int input = scanner.nextInt();
		        scanner.nextLine();
		        if (input == 0) return null;
		        if (input > 0 && input <= listOfFiles.length) {
			        File data = new File("src/edu/assignment/data/"+listOfFiles[input-1]);
			        readingFile = false;
			        return data;
		        }
		        else {
		        	System.out.println("Index out of bounds");
		        }
	        }
	        
	        return null;
	    }
	
	public static void chooseAlgorithm(Scanner scanner, File file) {
		boolean choosingAlgorithm = true;
		while (choosingAlgorithm == true) {
			choosingAlgorithm = false;
			System.out.println("Choose an algorithm to use: ");
			System.out.println("(1) Algorithm 1");
			System.out.println("(2) Algorithm 2");
			System.out.println("(3) Algorithm 3");
			System.out.println("(4) Algorithm 4");
			switch (scanner.nextLine().toUpperCase()){
			case "1": {
				System.out.println("execute algorithm 1");
				// algorithm 1 here
				break;

			}
			case "2": {
				System.out.println("execute algorithm 2");
				// algorithm 2 here
				break;

			}
			case "3": {
				System.out.println("execute algorithm 3");
				// algorithm 3 here
				break;

			}
			case "4": {
				System.out.println("execute algorithm 4");
				// algorithm 4 here
				break;

			}
			case "B" , "Q": {
				break;
			}
			default: {
				System.out.println("Invalid choice");	
				choosingAlgorithm = false;
				break;

			}
			}

		}
	}
	}

