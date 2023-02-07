import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class readInput {
	private String fileName;
	private String[] processAndResource;
	private String[] totalAndAvailable;
	private String[] maxAndAlloc;
	private int[] total;
	private int[] available;
	private int processes, resources;
	private int[][] max;
	private int[][] allocated;
	private int[][] need;
	private int[] allocTotal;
	
	public readInput() {
		
		//Scanner stdin = new Scanner(System.in);
//		System.out.println("enter file name: ");
//		fileName = stdin.next();
		
//		try (BufferedReader br = new BufferedReader(new FileReader("src/" + fileName))) {
		try (BufferedReader br = new BufferedReader(new FileReader("src/input1.txt"))) {
			int lineNumber = 0;
			boolean scanning = true;
			while (scanning) {
			switch (lineNumber) {
				case 0:
					scanNM(br);
					lineNumber++;
					total = new int[resources];
					available = new int[resources];
					max = new int[processes][resources];
					allocated = new int[processes][resources];
					need = new int[processes][resources];
					allocTotal = new int[resources];
					System.out.println("processes: "+ processes + " resources: " + resources);
					break;
				case 1:
					scanTotAvail(br);
					lineNumber++;
					break;
				default:
					scanMatrices(br);
					scanning = false;
			}
			}
		} catch (FileNotFoundException e) {		
			System.out.println("file not found");
		} catch (IOException e1) {
			System.out.println("error: IOException");
		}	
		
		
	}
	
	public void scanNM(BufferedReader br) {
		try {
			processAndResource = br.readLine().split(" ");
			if (Integer.parseInt(processAndResource[0]) < 10 && Integer.parseInt(processAndResource[0]) > 0) {
				processes = Integer.parseInt(processAndResource[0]);
			}
			else {
				System.out.println("number of processes out of range.");
			}
			if (Integer.parseInt(processAndResource[1]) < 10 && Integer.parseInt(processAndResource[1]) > 0) {
				resources = Integer.parseInt(processAndResource[1]);
			}
			else {
				System.out.println("number of resources out of range.");
			}
		} catch (IOException e) {
			System.out.println("error: IOException");
			System.exit(0);
		}
		catch (NumberFormatException e) {
			System.out.println("error: Input must be a number");
			System.exit(0);
		}
		catch (NullPointerException e) {
			System.out.println("Error: Null value found. ");
			System.exit(0);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("error: index out of bounds.");
			System.exit(0);
		}
		
	}
	
	public void scanTotAvail(BufferedReader br) {
		try {
			int count = 0;
			totalAndAvailable = br.readLine().split("\\s+");
			for(int i = 0; i < resources; i++) {
				total[i] = Integer.parseInt(totalAndAvailable[i]);
				count++;
			}
			
			System.out.println("total resources: " + Arrays.toString(total));
		} catch (IOException e) {
			System.out.println("error: IOException");
			System.exit(0);
		}
		catch (NumberFormatException e) {
			System.out.println("error: Input must be a number");
			System.exit(0);
		}
		catch (NullPointerException e) {
			System.out.println("Error: Null value found. ");
			System.exit(0);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("error: index out of bounds.");
			System.exit(0);
		}
		
	}
	
	public void scanMatrices(BufferedReader br) {
		try {
			int count = 0;
			// scan max
			for (int i = 0; i < processes; i++) {
				maxAndAlloc = br.readLine().split("\\s+");
				for (int j = 0; j < resources; j++) {
					max[i][j] = Integer.parseInt(maxAndAlloc[j]);
					count++;
				}
				
				for (int j = 0; j < resources; j++) {
					allocated[i][j] = Integer.parseInt(maxAndAlloc[count]);
					count++;
				}
				count = 0;
			}
			System.out.println(Arrays.deepToString(max) + " " + Arrays.deepToString(allocated));
			calculateNeed(max, allocated);
			sumColumns(allocated);
		}
		catch (IOException e) {
			System.out.println("error: IOException");
			System.exit(0);
		}
		catch (NumberFormatException e) {
			System.out.println("error: Input must be a number");
			//e.printStackTrace();
			System.exit(0);
		}
		catch (NullPointerException e) {
			System.out.println("Error: Null value found. ");
			System.exit(0);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("error: index out of bounds.");
			System.exit(0);
		}
	}
	
	public void calculateNeed(int[][] max, int[][] alloc) {
		for(int i = 0; i < processes; i++) {
			for(int j = 0; j < resources; j++) {
				if (max[i][j] - alloc[i][j] >= 0) {
					need[i][j] = max[i][j] - alloc[i][j];			
				}
				else {
					System.out.println("error: negative value found.");
					System.exit(0);
				}
			}
		}
		System.out.println(Arrays.deepToString(need));
	}
	
	public void sumColumns(int[][] alloc) {
		for (int i = 0; i < processes; i++) {
			for (int j = 0; j < resources; j++) {
				allocTotal[j] += alloc[i][j];
			}
		}
		System.out.println(Arrays.toString(allocTotal));
	}
	public void calculateTotal(int[][] available, int[][] alloc) {
		
	}
}
