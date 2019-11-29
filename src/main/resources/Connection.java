package main.resources;

public class Connection {
	public static final double INITIAL_WEIGHT = 1;
	private double weight;
	private int input;
	private int output;
	
	public Connection(int input, int output) {
		this.setInput(input);
		this.setOutput(output);
		this.setWeight(INITIAL_WEIGHT);
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
