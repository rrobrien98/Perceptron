package main;

import main.resources.Params;

import java.util.ArrayList;

import main.resources.Connection;
public class NN {
	
	private int[] input_nodes;
	private ArrayList<Connection> connections;
	private Params params;
	private double[] output_nodes;
	private int[] optimal_solution;
	public NN(Params params) {
		this.input_nodes = new int[this.params.getInput_nodes()];
		this.connections = new ArrayList<Connection>();
		this.output_nodes = new double[this.params.getOutput_nodes()];
		this.params = params;
		this.optimal_solution = null;
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
			this.setOptimalNodes();
			this.updateWeights();
		}
	}

	private void setOptimalNodes() {
		// TODO set the array of optimal nodes based on what the input is representing, could also be done in same
		// method as when we set the input nodes
		
	}

	private void updateWeights() {
		//for each connection, update its weight based on formula circled in red in group chat
		
	}

	private void setOutputNodes() {
		for (int i = 0; i < this.connections.size(); i++) {
			Connection connection = this.connections.get(i);
			this.output_nodes[connection.getOutput()] += this.input_nodes[connection.getInput()] * connection.getWeight();
		}
		for (int i = 0; i < this.output_nodes.length; i++) {
			this.output_nodes[i] = this.output_function(this.output_nodes[i]);
		}
		
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
		
	}

	private void setListNodes() {
		// TODO 
		
	}
	private double output_function(double input) {
		return Math.pow(1+Math.exp(-1*input + 0.5), -1);
	
	}
}
