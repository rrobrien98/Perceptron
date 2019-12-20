package main.resources;

import java.util.Random;
/*
 * Class to represent a connection between pair of input and output nodes
 */
public class Connection {
	public static final double INITIAL_WEIGHT = 0.3;//defines range over which connections should have initial weights
	public static Random rand = new Random();
	private double weight;
	private int input;
	private int output;
	
	/*
	 * Initializes connection object
	 * @param input: the index of the node in the input nodes array this connection is attached to
	 * @param output:the index of the node in the output nodes array this connection is attached to
	 */
	public Connection(int input, int output) {
		this.setInput(input);
		this.setOutput(output);
		//Weight set randomly in initialization range
		this.setWeight((rand.nextDouble()*INITIAL_WEIGHT) - INITIAL_WEIGHT/2.0);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getInput() {
		return input;
	}

	public void setInput(int input) {
		this.input = input;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

}