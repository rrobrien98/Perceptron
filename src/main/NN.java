package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import main.resources.*;
public class NN { 
	private static final int LIST_NODES = 64;
	private int[] input_nodes;
	private ArrayList<Connection> connections; 
	private Params params;
	private double[] output_nodes;
	private double[] optimal_solution;
	private int passed_tests;
	private int[] solution;
	
	private int[][] train_data;
	public NN(Params params) {
		this.params = params;
		this.input_nodes = new int[this.params.getInput_nodes()];
		this.connections = new ArrayList<Connection>();
		this.output_nodes = new double[this.params.getOutput_nodes()];
		this.optimal_solution = new double[this.params.getOutput_nodes()];
		this.solution = new int[this.params.getIterations()];
		this.train_data = new int[this.params.getIterations()][this.params.getInput_nodes()];
		this.initializeConnections();
		this.trainNN();


		this.passed_tests = 0;
	}
	
	private void initializeConnections() {
		for (int i = 0; i < this.params.getInput_nodes(); i++) {
			for (int j = 0; j < this.params.getOutput_nodes(); j++) {
				connections.add(new Connection(i,j));
			}
		}
		
	}

	private void trainNN() {
		int iterations = 0;
		this.initInput(this.params.getFilename());
		this.setOptimalNodes(solution[iterations]);
		while (this.params.getIterations()>iterations) {
			this.setInputNodes(iterations);
			this.setOptimalNodes(solution[iterations]);
			this.setOutputNodes();
			this.updateWeights();
			iterations++;
			System.out.println(this.output_nodes[0]);
		}
	}



	private void testNN() {
		int tests = 0;
		this.initInput(this.params.getTestfile());
		while (this.params.getTests()>tests) {
			this.setInputNodes(tests);
			this.setOutputNodes();
			this.getClassification(solution[tests]);
			
		}
	}	
	private void setInputNodes(int iteration) {
		// TODO Auto-generated method stub
		this.input_nodes = this.train_data[iteration];
	}
	private void getClassification(int iteration) {
		// TODO Auto-generated method stub
		switch(this.params.getOutput_nodes()) {
		
		case 10:
			this.singleClassification(iteration);
			break;
		case 1:
			this.decimalClassification(iteration);
			break;
		default:
			System.out.println("Unrecognized output type");
			break;
		}
		
	}

	private void decimalClassification(int iteration) {
		// TODO Auto-generated method stub
		int max_index = 0;
		double max_val = Double.MAX_VALUE;
		for (int i = 0; i < this.output_nodes.length; i++) {
			if (this.output_nodes[i] > max_val) {
				max_val = this.output_nodes[i];
				max_index = i;
			}
		}
		if (max_index == this.solution[iteration]) {
			this.passed_tests++;
		} 
	}

	private void singleClassification(int iteration) {
		// TODO Auto-generated method stub
		if (this.solution[iteration] == Math.round(10*this.output_nodes[0])) {
			this.passed_tests++;
		}
	}

	private void setOptimalNodes(int solution) {
		// TODO set the array of optimal nodes based on what the input is representing, could also be done in same
		// method as when we set the input nodes
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

	private void setDecimalOutput(int solution) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.optimal_solution.length; i++) {
			if (i == solution) {
				this.optimal_solution[i] = 1;
			}
			else {
				this.optimal_solution[i] = 0;
			}
		}
	}

	private void setSingleOutput(int solution) {
		// TODO Auto-generated method stub
		this.optimal_solution[0] = (solution * 0.1); 
	}

	private void updateWeights() {
		//for each connection, update its weight based on formula circled in red in group chat
		for (int i = 0; i<this.connections.size();i++) {
			Connection connection = this.connections.get(i);
			double err = this.optimal_solution[connection.getOutput()] - this.output_function(this.output_nodes[connection.getOutput()]);
			connection.setWeight(connection.getWeight()+(err*this.output_function_deriv(connection.getOutput())*this.input_nodes[connection.getInput()]*this.params.getLr()));
		}
	}

	private void setOutputNodes() {
		for (int i = 0; i < this.connections.size(); i++) {
			Connection connection = this.connections.get(i);
			this.output_nodes[connection.getOutput()] += this.input_nodes[connection.getInput()] * connection.getWeight();
		}
		//just set output nodes as sum of inputs
		//for (int i = 0; i < this.output_nodes.length; i++) {
		//	this.output_nodes[i] = this.output_function(this.output_nodes[i]);
		//}
		
	}

	private void initInput(String filename) {
		switch(this.params.getInput_nodes()) {
			case 32*32:
				//parse 32x32 bit array, one node created for value of each bit
				this.initArrayInput(filename);
				break;
			case 64:
				//parse 64 int list, one node for each int
				this.initListInput(filename);
				break;
			default:
				System.out.println("Unrecognized input type");
				break;
		}
				
		
	}
	
	//connection object
		//wieght
		//input node
		//output node
		
	
		// output nodes
			// 
	
	
		// update nodes
			// calculate output values
			// loop through output nodes
				// through connections made to it
					// 
		//



	private void initArrayInput(String filename) {
		// TODO 
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (int j = 0; j < this.params.getIterations(); j++) {
				int array_index = 0;
				String line = br.readLine().trim();
				while(line.length()!=1) {//go until classification
					for (int i = 0; i<line.length();i++) {
						this.train_data[j][array_index] = line.charAt(i);
						array_index++;
					}
					line = br.readLine().trim();
				}
				this.solution[j] = Integer.parseInt(line);
				
			}
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
	}

	private void initListInput(String filename) {
		// TODO 
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			for (int j = 0; j < this.params.getIterations(); j++) {
				String line = br.readLine().trim();
				String[] vals = line.split(",");
				for (int i = 0; i < LIST_NODES; i++){//stop cases are EOF or -1
					//once the first city is reached start recording cities.
					
					this.train_data[j][i]= Integer.parseInt(vals[i]);
				}
				this.solution[j] = Integer.parseInt(vals[vals.length-1]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
		
	}
	private double output_function(double input) {
		return Math.pow(1+Math.exp(-1*input + 0.5), -1);
	
	}
	private double output_function_deriv(double input) {
		return Math.pow(1+Math.exp(-1*input + 0.5), -2) *  Math.exp(-1*input + 0.5);
	}
	public int getPassedTests() {
		return this.passed_tests;
	}
}
