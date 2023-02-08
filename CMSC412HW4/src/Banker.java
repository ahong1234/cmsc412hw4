import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Banker {
	public String fileName;
	public String[] processAndResource;
	public String[] totalAndAvailable;
	public String[] maxAndAlloc;
	public int[] total;
	public int[] available;
	public int processes, resources;
	public int[][] max;
	public int[][] allocated;
	public int[][] need;
	public int[] allocTotal;
	public int[] work;
	public boolean[] finish;
	public int[] seq;
	
	
	public Banker() {
		
		//Scanner stdin = new Scanner(System.in);
//		System.out.println("enter file name: ");
//		fileName = stdin.next();
		
//		try (BufferedReader br = new BufferedReader(new FileReader("src/" + fileName))) {
		try (BufferedReader br = new BufferedReader(new FileReader("src/input3.txt"))) {
			int lineNumber = 0;
			boolean scanning = true;
			while (scanning) {
			switch (lineNumber) {
				case 0:
					// scan first line for N and M
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
					// scan second line for total available resources
					scanTotAvail(br);
					lineNumber++;
					break;
				default:
					// scan max and allocated matrices
					scanMatrices(br);
					// calculate need and available matrices
					calculateNeed(max, allocated);
					sumColumns(allocated);
					calculateAvailable(total, allocTotal);
					printMatrice(max, allocated);
					if(findSequence(available, allocated)) {
						System.out.println(Arrays.toString(work));
						System.out.println("Sequence found: "+ Arrays.toString(seq));
					}
					else {
						System.out.println("no sequence found");
					}
					
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
			
			totalAndAvailable = br.readLine().split("\\s+");
			
			for(int i = 0; i < resources; i++) {
				total[i] = Integer.parseInt(totalAndAvailable[i]);
				
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
	
	}
	
	public void sumColumns(int[][] alloc) {
		for (int i = 0; i < processes; i++) {
			for (int j = 0; j < resources; j++) {
				allocTotal[j] += alloc[i][j];
			}
		}
	
	}
	
	public void calculateAvailable(int[] total, int[] alloc) {
		for(int i = 0; i < resources; i++) {
			available[i] = total[i] - alloc[i];
		}
	
	}
	public void printMatrice(int[][] max, int[][] alloc) {
		for(int i = 0; i < processes; i++) {
			String line = "";
			for(int j = 0; j < resources; j++) {
				line += max[i][j] + " ";
			}
			line += " ";
			for(int j = 0; j < resources; j++) {
				line += alloc[i][j] + " ";
			}
			System.out.println(line);
		}
	}
	
	public boolean findSequence(int[] available, int[][] allocation) {
		seq = new int[processes];
		boolean found = false;
		boolean processGood = false;
		work = available;
		finish = new boolean[processes];
		for (int i = 0; i < processes; i++) {
			finish[i] = false;
		}
		int count = 0;
			while (count < processes) {
				
			// scan processes
				
				for (int i = 0; i < processes; i++) {
					System.out.println(i);
					if (!finish[i]) {
						int j = 0;
						for (; j < resources; j++) {
							if (need[i][j] > work[j]) {
								System.out.println("process: " + i);
								break;
							}
							if ((j+1) == resources) {
								
								for (int k = 0; k < resources; k++) {
						              work[k] = work[k] + allocation[i][k];
						              
						        }
								System.out.println(Arrays.toString(work));
								seq[count++] = (i);
								finish[i] = true;
								found = true;
								
								
							}
					 }
						
				   }
					
				}
				if (!found) {		
					return false;
				}
			}
			return true;	
	}
	
	public static void main(String[] args) throws IOException {
		Banker bk = new Banker();
		
	}
}
