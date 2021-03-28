package socialnetworkservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
	private List<Node> edges = new ArrayList<>();
	private int value;

	private float sendPercentage = 0.7f;
	private boolean hasData;

	public Node(int value) {
		this.value = value;
	}

	public void addEdge(Node node) {
		edges.add(node);
	}

	public void setSendPercentage(float percentage) {
		this.sendPercentage = percentage;
	}

	public void setData(boolean hasData) {
		this.hasData = hasData;
	}

	public List<Node> getEdges() {
		return edges;
	}

	public List<Node> getTFPossibleEdges(Node inputNode) {
		List<Node> tfPossibleEdges = new ArrayList<>();
		for (Node edge : edges)
			if (!edge.contains(inputNode) && !edge.equals(inputNode))
				tfPossibleEdges.add(edge);
		return tfPossibleEdges;
	}

	public boolean contains(Node node) {
		return edges.contains(node);
	}

	public int getValue() {
		return value;
	}

	public int size() {
		return edges.size();
	}

	public float getSendPercentage() {
		return sendPercentage;
	}

	public boolean hasData() {
		return hasData;
	}

	private float cascadePercentage() {
		int dataCnt = 0;
		for (Node edge : edges)
			if (edge.hasData)
				dataCnt++;
		return dataCnt * 100f / edges.size();
	}

	private Random random = new Random();

	public void linearSpreadData() {
		for (Node edge : edges)
			if (!edge.hasData && getRandomFloatPercentage() <= sendPercentage * 100)
				edge.setData(true);
	}

	public void cascadeSpreadData() {
		for (Node edge : edges)
			if (!edge.hasData && getRandomFloatPercentage() <= edge.cascadePercentage())
				edge.setData(true);
	}

	private float getRandomFloatPercentage() {
		return random.nextFloat() * 100;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		return ((Node) obj).getValue() == getValue();
	}
}
