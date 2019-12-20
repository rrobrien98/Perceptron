package main;

import main.resources.Params;

//Constructs and trains a perceptron from the parameters passed in over command line
public class Lab {
	/*
	 * Constructs params object from command line arguments
	 * Initializes NN object with this params object
	 * Runs tests on NN object and reports the number of classifications it gets right
	 */
	public static void main(String[] args) {
		
		int input_nodes = Integer.parseInt(args[0]);//number of input nodes, 64 or 1024
		int output_nodes = Integer.parseInt(args[1]);//number of output nodes, 10 or 1
		String filename = args[2];//filename of train data
		int iterations = Integer.parseInt(args[3]);//number of iterations to train for
		double lr = Double.parseDouble(args[4]);//learning rate
		int tests = Integer.parseInt(args[5]);//number of tests to run
		String testfile = args[6];//name of file containing test data
		
		Params params = new Params(input_nodes, output_nodes, filename, iterations, lr, tests, testfile);
		NN test = new NN(params);
		test.testNN();
		System.out.println("results");
		System.out.println((double) test.getPassedTests());
		
	}

}
