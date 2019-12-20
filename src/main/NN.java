package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import main.resources.*;
/**
 * Class that runs our perceptron neural network
 * Handles data parsing, training, and testing
 *
 */
public class NN { 
	private static final int LIST_NODES = 64;//number of integers to parse from 8x8 input
	private double[] input_nodes;//holds the data for the input nodes for current epoch/test
	private ArrayList<Connection> connections; //Contains all connections between input/output
	private Params params;
	private double[] output_nodes;//holds values of output nodes
	private double[] optimal_solution;//set each epoch based on solution parsed for each data set
	private int passed_tests;//keeps track of correct classifications
	private double[] solution;//holds all of the solutions to each classification problem
	private double[][] train_data;//holds all of data we need for either testing or training
	/*
	 * Initializes and trains the neural network
	 * takes in params file that specifies all details of running
	 */
	public NN(Params params) {
		this.passed_tests = 0;
		this.params = params;
		this.input_nodes = new double[this.params.getInput_nodes()+1];
		this.connections = new ArrayList<Connection>();
		this.output_nodes = new double[this.params.getOutput_nodes()];
		this.optimal_solution = new double[this.params.getOutput_nodes()];
		//both solution and train_data will hold data for both testing and training, so make size big enough for either
		this.solution = new double[Math.max(this.params.getIterations(),this.params.getTests())];
		this.train_data = new double[Math.max(this.params.getIterations(),this.params.getTests())][this.params.getInput_nodes()];
		this.initializeConnections();
		this.trainNN();


		
	}
	/*
	 * Creates connection between each input and output node
	 */
	private void initializeConnections() {
		for (int i = 0; i < this.params.getInput_nodes()+1; i++) {
			for (int j = 0; j < this.params.getOutput_nodes(); j++) {
				connections.add(new Connection(i,j));
			}
		}
		
	}
	/*
	 * Trains network for number of epochs specified by params object
	 */
	private void trainNN() {
		int iterations = 0;
		this.initInput(this.params.getFilename(),this.params.getIterations());
		while (this.params.getIterations()>iterations) {
			//iterations used as param in these functions as they set the input and optimal nodes from arrays that contain all training data
			this.setInputNodes(iterations);
			this.setOptimalNodes(solution[iterations]);
			this.setOutputNodes();
			this.updateWeights();
			iterations++;
		}
	}


	/*
	 * Runs test data through trained network and gets classification after each test
	 */
	public void testNN() {
		int tests = 0;
		this.initInput(this.params.getTestfile(), this.params.getTests());
		while (this.params.getTests()>tests) {
			this.setInputNodes(tests);
			this.setOutputNodes();
			this.getClassification(tests);
			tests++;
		}
	}	
	
	/*
	 * Grabs the proper array of input values from train_data, which contains many rounds worth of data parsed from file
	 * @param iteration: used to index into the train_data array
	 */
	private void setInputNodes(int iteration) {
		for (int i = 0; i < this.train_data[iteration].length;i++) {
			this.input_nodes[i] = this.train_data[iteration][i];
		}
		//set bias node to 1 always
		this.input_nodes[this.params.getInput_nodes()] = 1;
	}
	
	/*
	 * Calls the appropriate classification method based on topology
	 * @param iteration: Needed to index into array of all solutions parsed from file
	 */
	private void getClassification(int iteration) {
		switch(this.params.getOutput_nodes()) {
		case 1:
			this.singleClassification(iteration);
			break;
		case 10:
			this.decimalClassification(iteration);
			break;
		default:
			System.out.println("Unrecognized output type");
			break;
		}
		
	}
	/*
	 * Used for 10 output node topology
	 * finds the index of the max of the output nodes and increments passed tests if this equals desired outcome
	 */
	private void decimalClassification(int iteration) {
		int max_index = 0;
		double max_val = 0;
		for (int i = 0; i < this.output_nodes.length; i++) {
			if (this.output_function(this.output_nodes[i]) > max_val) {
				max_val = this.output_function(this.output_nodes[i]);
				max_index = i;
			}
		}
		//iteration finds index in solution array of current input data
		if (max_index == this.solution[iteration]) {
			this.passed_tests++;
		} 
	}
	/*
	 * Used for the 1 node output topology
	 * Interprates output as 10 times the output node rounded to the nearest integer
	 * Increments passed tests if classification is correct
	 */
	private void singleClassification(int iteration) {
		if (this.solution[iteration] == Math.round(10.0*this.output_function(this.output_nodes[0])-0.5)) {
			System.out.println(this.passed_tests);
			this.passed_tests++;
		}
	}
	
	/*
	 * Calls the appropriate method to update the optimal solution vector
	 * @param solution: solution value that should be represented by optimal nodes
	 */
	private void setOptimalNodes(double solution) {
		switch(this.params.getOutput_nodes()) {
		case 10:
			this.setDecimalOutput(solution);
			break;
		case 1:
			this.setSingleOutput(solution);
			break;
		default:
			System.out.println("Unrecognized output type");
			break;
		}
		
	}
	/*
	 * Used for 10 output node topology
	 * sets value at the index of solution in optimal nodes to be 1, the rest to be 0
	 */
	private void setDecimalOutput(double solution) {
		for (int i = 0; i < this.optimal_solution.length; i++) {
			if (i == solution) {
				this.optimal_solution[i] = (double) 1.0;
			}
			else {
				this.optimal_solution[i] = (double) 0.0;
			}
		}
	}
	/*
	 * used for single node output topology
	 * Sets the value of the node to be one tenth the solution value given
	 */
	private void setSingleOutput(double solution) {
		this.optimal_solution[0] = (solution * 0.1); 
	}
	
	/*
	 * Updates the weights of all connections based on the values of the input nodes, output nodes, 
	 * and optimal solution for this epoch
	 * Math is according to equation 3 in paper
	 */
	private void updateWeights() {
		for (int i = 0; i<this.connections.size();i++) {
			Connection connection = this.connections.get(i);
			//note that output nodes simply contains summations of inputs, that is why we call output_function here
			double err = this.optimal_solution[connection.getOutput()] - this.output_function(this.output_nodes[connection.getOutput()]);
			connection.setWeight(connection.getWeight()+(err*this.output_function_deriv(this.output_nodes[connection.getOutput()])*this.input_nodes[connection.getInput()]*this.params.getLr()));
		}
	}
	/*
	 * calculates the values of output function based on the inputs and weight of connection to it
	 */
	private void setOutputNodes() {
		//reset all to zero to ignore previous iterations
		for (int i = 0; i < this.output_nodes.length; i++) {
			this.output_nodes[i] = 0.0;
		}
		//output calculated with equation 1 in paper
		for (int i = 0; i < this.connections.size(); i++) {
			Connection connection = this.connections.get(i);
			this.output_nodes[connection.getOutput()] += (this.input_nodes[connection.getInput()] * connection.getWeight());
		}
		
	}
	/*
	 * Calls appropriate function to parse in all necessary data from file
	 * Used by both the testing and training routines
	 * @param filename: file to be parsed from
	 * @param iterations: number of representations of numbers and their solutions to be parsed in
	 */
	private void initInput(String filename, int iterations) {
		switch(this.params.getInput_nodes()) {
			case 32*32:
				//parse 32x32 bit array, one node created for value of each bit
				this.initArrayInput(filename, iterations);
				break;
			case 64:
				//parse 64 int list, one node for each int
				this.initListInput(filename, iterations);
				break;
			default:
				System.out.println("Unrecognized input type");
				break;
		}
				
		
	}
	/*
	 * Parse in 32x32 bit data into train data and solution array for given num of iterations
	 * 
	 */
	private void initArrayInput(String filename, int iterations) {
		
		try { 
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (int j = 0; j < iterations; j++) {
				//array_index keeps track of our position in the current row of train data, which has length of 1024
				int array_index = 0;
				String line = br.readLine().trim();
				while(line.length()!=1) {//go until classification/solution
					
					for (int i = 0; i<line.length();i++) {
						//charAt returns ASCII values, so need to adjust by subtracting 48
						this.train_data[j][array_index] = (double) line.charAt(i) - 48;
						array_index++;
					}
					line = br.readLine().trim();
				}
				//capture solution from this index
				this.solution[j] = Double.parseDouble(line);
				
			}
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
	}
	/*
	 * Parse in 8x8 integer data into train data and solution array for given num of iterations
	 */
	private void initListInput(String filename, int iterations) {
		// TODO 
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			for (int j = 0; j < iterations; j++) {
				//single line contains all data and classification
				String line = br.readLine().trim();
				String[] vals = line.split(",");
				for (int i = 0; i < LIST_NODES; i++){
				
					
					this.train_data[j][i]= Double.parseDouble(vals[i]);
				}
				//capture solution
				this.solution[j] = Double.parseDouble(vals[vals.length-1]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
		
	}
	/*
	 * Sigmoid function
	 */
	private double output_function(double input) {
		//0.5 added because it produced the best results
		return 1.0/(1+Math.exp(-1.0*input + 0.5));
	
	}
	/*
	 * Derivative of sigmoid function
	 */
	private double output_function_deriv(double input) {
		return output_function(input)*(1-output_function(input));
	}
	/*
	 * Getter for number of tests passed
	 */
	public int getPassedTests() {
		return this.passed_tests;
	}
}
