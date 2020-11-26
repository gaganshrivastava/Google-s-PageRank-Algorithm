

import static java.lang.Math.abs;

import java.awt.Container;
import java.io.File;
import java.util.Scanner;

public class Pgrk0990 {

	static double errorrate = 0.00001; // default error rate as per Prp
	static double d = 0.85;
	static int numOfVertices;
	static int numOfEdges;
	static int[][] adjcancyMatrix;
	static int iterations;
	static int initialvalue;
	// static String filename;
	static int[] C;
	static double[] auth;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		try {
			 if (args.length != 3) {
			 System.out.println("Please provide valid running comd");
			 return;
			 }
			// Parsing the command-line arguments
			 iterations = Integer.parseInt(args[0]);
			 initialvalue = Integer.parseInt(args[1]);
			 String filename = args[2];
			/*iterations = 15;
			initialvalue = -1;
			String filename = "samplegraph2.txt";*/

			if (!(initialvalue >= -2 && initialvalue <= 1)) {
				System.out.println("Initialization value must be between -2 and 1");
				return;
			}

			File file = new File(filename);
			Scanner fileData = new Scanner(file);

			// ---------------------------------------
			// Getting the number of vertices and edges in the graph
			try {
				if (fileData.hasNextLine()) {
					String[] parts = fileData.nextLine().split(" "); // taking
																		// the
																		// first
																		// row
					numOfVertices = Integer.parseInt(parts[0]);
					numOfEdges = Integer.parseInt(parts[1]);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Invalid number of vertices and edges");
			}
			// System.out.println("number of vertices = " + numOfVertices);
			// System.out.println("number of edges = " + numOfEdges);

			// ---------------------------------------
			// Making the adjacency Matrix
			adjcancyMatrix = new int[numOfVertices][numOfVertices];

			try {
				String[] parts = new String[2]; // to store the values of file
				while (fileData.hasNext()) {
					String data = fileData.nextLine();
					parts = data.split(" ", 2);

					int n = Integer.parseInt(parts[0]);
					int m = Integer.parseInt(parts[1]);
					adjcancyMatrix[n][m] = 1;

				}
			} catch (Exception e) {
				System.out.println("Invalid data");
			}

			// printing adjaceancy matrix
			/*
			 * for (int a[] : adjcancyMatrix) { for (int x : a) {
			 * System.out.print(x + " "); } System.out.println(); }
			 */
			// ---------------------------------------
			// Making Hub and Authority
			// hub0 = new double[numOfVertices];
			auth = new double[numOfVertices];

			switch (initialvalue) { // init the hub and auth values for 0th
									// iteration

			case -1:
				for (int i = 0; i < numOfVertices; i++) {
					// hub0[i] = 1.0 / numOfVertices;
					auth[i] = 1.0 / numOfVertices; // 1/n
				}
				break;

			case -2:
				for (int i = 0; i < numOfVertices; i++) {
					// hub0[i] = 1.0 / Math.sqrt(numOfVertices); // 1/root(n)
					auth[i] = 1.0 / Math.sqrt(numOfVertices);
				}
				break;

			case 1:
				for (int i = 0; i < numOfVertices; i++) {
					// hub0[i] = 1;
					auth[i] = 1;
				}
				break;

			case 0:
				for (int i = 0; i < numOfVertices; i++) {
					// hub0[i] = 0;
					auth[i] = 0;
				}
				break;
			}

			// C[i] calculation. C[i] matrix is the out-degree of vertex Ti
			C = new int[numOfVertices];
			for (int i = 0; i < numOfVertices; i++) {
				C[i] = 0;
				for (int j = 0; j < numOfVertices; j++) {
					C[i] += adjcancyMatrix[i][j];

				}
			}

			// printing C array = outdegreee
			/*
			 * for(int x : C){ System.out.print( x + " "); }
			 */

			googlePR();

			fileData.close();

		} catch (Exception e) {
			System.out.println("File Not found");
		}

	}

	public static void googlePR() {
		double[] Pr = new double[numOfVertices];
		int conatains = 1;

		if (numOfVertices > 10) {
			iterations = 0;
			for (int k = 0; k < numOfVertices; k++) { // 1/N
				auth[k] = 1.0 / numOfVertices;
			}
			int i = 0;

			do {
				if (conatains == 1) {
					conatains = 0;
				} else {
					for (int x = 0; x < numOfVertices; x++) {
						auth[x] = Pr[x];
					}
				}
				for (int y = 0; y < numOfVertices; y++) {
					Pr[y] = 0.0;
				}

				for (int y = 0; y < numOfVertices; y++) {
					for (int z = 0; z < numOfVertices; z++) {
						if (adjcancyMatrix[z][y] == 1) {
							Pr[y] += auth[z] / C[z];
						}
					}
				}

				// using the formula
				for (int x = 0; x < numOfVertices; x++) {
					Pr[x] = d * Pr[x] + (1 - d) / numOfVertices;
				}
				i++;
			} while (cmpErrorRate(auth, Pr) != true);

			System.out.println("Iteration Number when converged: " + i);
			for (int l = 0; l < numOfVertices; l++) {
				System.out.printf("P[" + l + "] = %.7f\n", Math.round(Pr[l] * 10000000.0) / 10000000.0);
			}
			return;
		}

		// for 0th iteration
		System.out.print("Base    : 0");
		for (int j = 0; j < numOfVertices; j++) {
			System.out.printf(" :P[ " + j + "]=%.7f", Math.round(auth[j] * 10000000.0) / 10000000.0);
		}

		if (iterations > 0) {
			for (int i = 0; i < iterations; i++) {
				for (int j = 0; j < numOfVertices; j++) {
					Pr[j] = 0.0;
				}

				for (int j = 0; j < numOfVertices; j++) {
					for (int k = 0; k < numOfVertices; k++) {
						if (adjcancyMatrix[k][j] == 1) {
							Pr[j] += auth[k] / C[k];

						}
					}
				}

				// formula and printing the iterations
				System.out.println();
				System.out.print("Iter    : " + (i + 1));
				for (int x = 0; x < numOfVertices; x++) {
					Pr[x] = d * Pr[x] + (1 - d) / numOfVertices;
					System.out.printf(" :P[ " + x + "]=%.7f", Math.round(Pr[x] * 10000000.0) / 10000000.0);
				}

				for (int x = 0; x < numOfVertices; x++) {
					auth[x] = Pr[x];
				}
			}
			System.out.println();
		} else {
			// for negative iteration value

			if (iterations == 0) {
				errorrate = Math.pow(10, -5); // for iteration is 0, default
												// errorrate 10^-5
			} else {
				errorrate = Math.pow(10, iterations);
			}
			int i = 0;
			do {
				if (conatains == 1) {
					conatains = 0;
				} else {
					for (int x = 0; x < numOfVertices; x++) {
						auth[x] = Pr[x];
					}
				}
				for (int x = 0; x < numOfVertices; x++) {
					Pr[x] = 0.0;
				}

				for (int j = 0; j < numOfVertices; j++) {
					for (int k = 0; k < numOfVertices; k++) {
						if (adjcancyMatrix[k][j] == 1) {
							Pr[j] += auth[k] / C[k];
						}
					}
				}

				// print the itteration values
				System.out.println();
				System.out.print("Iter    : " + (i + 1));
				for (int x = 0; x < numOfVertices; x++) {
					Pr[x] = d * Pr[x] + (1 - d) / numOfVertices;
					System.out.printf(" :P[ " + x + "]=%.7f", Math.round(Pr[x] * 10000000.0) / 10000000.0);
				}
				i++;
			} while (cmpErrorRate(auth, Pr) != true);
			System.out.println();
		}

	}

	public static boolean cmpErrorRate(double[] curr, double[] pre) {

		// comparing the differecne between the prev and curr for each edges
		for (int i = 0; i < numOfVertices; i++) {
			if (abs(curr[i] - pre[i]) > errorrate)
				return false;
		}
		return true;
	}

}
