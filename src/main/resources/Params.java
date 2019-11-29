package main.resources;

public class Params {
//filename, type of input, type of output, number of iterations
	private int input_nodes;
	private int output_nodes;
	private String filename;
	private int iterations;
	public Params(int input_nodes, int output_nodes, String filename, int iterations) {
		this.input_nodes = input_nodes;
		this.output_nodes = output_nodes;
		this.filename=filename;
		this.iterations=iterations;
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
}
