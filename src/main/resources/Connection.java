package main.resources;

import java.util.Random;

public class Connection {
	public static final double INITIAL_WEIGHT = 0.3;
	public static Random rand = new Random();
	private double weight;
	private int input;
	private int output;
	
	public Connection(int input, int output) {
		this.setInput(input);
		this.setOutput(output);
		this.setWeight((rand.nextDouble()*INITIAL_WEIGHT) - 0.15);
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