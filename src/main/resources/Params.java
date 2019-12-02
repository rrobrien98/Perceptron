package main.resources;

public class Params {
//filename, type of input, type of output, number of iterations
	private int input_nodes;
	private int output_nodes;
	private String filename;
	private int iterations;
	private double lr;
	private int optimal_rep;
	public Params(int input_nodes, int output_nodes, String filename, int iterations, double lr, int optimal_rep) {
		this.input_nodes = input_nodes;
		this.output_nodes = output_nodes;
		this.filename=filename;
		this.iterations=iterations;
		this.lr = lr;
		this.optimal_rep=optimal_rep;
	}

	public String getFilename() {
		return filename;
	}
	public int getIterations() {
		return iterations;
	}

	public int getOutput_nodes() {
		return output_nodes;
	}

	public int getInput_nodes() {
		return input_nodes;
	}

	public double getLr() {
		return lr;
	}

	public int getOptimal_rep() {
		return optimal_rep;
	}
}
