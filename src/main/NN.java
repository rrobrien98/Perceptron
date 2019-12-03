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
	public NN(Params params) {
		this.input_nodes = new int[this.params.getInput_nodes()];
		this.connections = new ArrayList<Connection>();
		this.output_nodes = new double[this.params.getOutput_nodes()];
		this.params = params;
		this.optimal_solution = new double[this.params.getOutput_nodes()];
		this.initializeConnections();
		this.trainNN();
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
		while (this.params.getIterations()>iterations) {
			this.setInputNodes();
			this.setOutputNodes();
			this.updateWeights();
		}
	}
	private void testNN() {
		
	}
	private void setOptimalNodes(int solution) {
		// TODO set the array of optimal nodes based on what the input is representing, could also be done in same
		// method as when we set the input nodes
		switch(this.params.getOutput_nodes()) {
		case 10:
			this.setSingleOutput(solution);
		case 1:
			this.setDecimalOutput(solution);
		default:
			System.out.println("Unrecognized output type");
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

	private void setInputNodes() {
		switch(this.params.getInput_nodes()) {
			case 32*32:
				//parse 32x32 bit array, one node created for value of each bit
				this.setArrayNodes();
			case 64:
				//parse 64 int list, one node for each int
				this.setListNodes();
			default:
				System.out.println("Unrecognized input type");
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



	private void setArrayNodes() {
		// TODO 
		try {
			int array_index = 0;
			BufferedReader br = new BufferedReader(new FileReader(this.params.getFilename()));
			String line = br.readLine().trim();
			while(line.length()!=1) {//go until classification
				for (int i = 0; i<line.length();i++) {
					this.input_nodes[array_index] = line.charAt(i);
					array_index++;
				}
				line = br.readLine().trim();
			}
			this.setOptimalNodes(Integer.parseInt(line));
			br.close();
		} catch (FileNotFoundException e) {
			System.out.print("Input File Not Found.");
		} catch (IOException e) {
			System.out.print("IO error in br.readline()");
			e.printStackTrace();
		}
	}

	private void setListNodes() {
		// TODO 
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.params.getFilename()));
			String line = br.readLine().trim();
			for (int i = 0; i < LIST_NODES; i++){//stop cases are EOF or -1
				//once the first city is reached start recording cities.
				this.input_nodes[i] = line.charAt(i);

			}
			this.setOptimalNodes(line.charAt(LIST_NODES));
			//MAY NEED TO KEEP TRACK OF POSITION IN FILE
			br.close();
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
		return Math.pow(1+Math.exp(-1*input + 0.5), -2) * -1 * Math.exp(-1*input + 0.5);
	}
}
