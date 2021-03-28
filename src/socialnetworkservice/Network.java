package socialnetworkservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Network {
	private long k = 0;
	private float pt = 0.3f;
	private Node tfNode = null;
	private Random random = new Random();
	private List<Node> nodes = new ArrayList<>();

	public Network() {
		init();
	}

	public void setK(long k) {
		this.k = k;
	}

	public void setPT(float pt) {
		this.pt = pt;
	}

	public void addNode(int value) {
		int edgeCnt = getRandomEdgeCnt();
		edgeCnt = edgeCnt > nodes.size() ? nodes.size() : edgeCnt;
		Node input = new Node(value);
		for (int i = 0; i < edgeCnt; i++)
			if (tfNode != null)
				tf(input);
			else
				pa(input);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public boolean hasAllNodesData() {
		for (Node node : nodes)
			if (!node.hasData())
				return false;
		return true;
	}

	public void initHasData() {
		for (Node node : nodes)
			node.setData(false);
	}

	private int linearSpreadCnt;

	public void doLinearSpread() {
		List<Node> linearNodes = new ArrayList<>();
		for (Node node : nodes)
			if (node.hasData())
				linearNodes.add(node);
		for (Node node : linearNodes)
			node.linearSpreadData();
		linearSpreadCnt++;
	}

	public int getLinearSpreadCnt() {
		return linearSpreadCnt;
	}

	public void reSetLinearSpread() {
		linearSpreadCnt = 0;
	}

	private int cascadeSpreadCnt;

	public void doCascadeSpread() {
		List<Node> cascadeNodes = new ArrayList<>();
		for (Node node : nodes)
			if (node.hasData())
				cascadeNodes.add(node);
		for (Node node : cascadeNodes)
			node.cascadeSpreadData();
		cascadeSpreadCnt++;
	}

	public int getCascadeSpreadCnt() {
		return cascadeSpreadCnt;
	}

	public void reSetCascadeSpread() {
		cascadeSpreadCnt = 0;
	}

	public Map<Integer, Integer> getEdgeNodePair() {
		Map<Integer, Integer> rv = new HashMap<>();
		for (Node node : nodes)
			if (rv.containsKey(node.getEdges().size()))
				rv.put(node.getEdges().size(), rv.remove(node.getEdges().size()) + 1);
			else
				rv.put(node.getEdges().size(), 1);
		return rv;
	}

	private void init() {
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);

		node1.addEdge(node2);
		node1.addEdge(node3);

		node2.addEdge(node1);
		node2.addEdge(node3);

		node3.addEdge(node1);
		node3.addEdge(node2);

		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
	}

	private void pa(Node inputNode) {
		List<Node> paPossibleNodes = getPAPossibleNodes(inputNode);
		Node conn = paPossibleNodes.get(getPercentageIdx(paPossibleNodes));
		conn.addEdge(inputNode);
		inputNode.addEdge(conn);
		if (!nodes.contains(inputNode))
			nodes.add(inputNode);
		tfNode = conn;
	}

	private void tf(Node inputNode) {
		if (getRandomFloatPercentage() <= pt * 100) {
			List<Node> tfPossibleEdges = tfNode.getTFPossibleEdges(inputNode);
			if (tfPossibleEdges.isEmpty())
				pa(inputNode);
			else {
				Node conn = tfPossibleEdges.get(getRandomIdx(tfPossibleEdges.size()));
				conn.addEdge(inputNode);
				inputNode.addEdge(conn);
				if (!nodes.contains(inputNode))
					nodes.add(inputNode);
				tfNode = null;
			}
		} else
			pa(inputNode);
	}

	private int getTotalEdgesCnt() {
		int totalEdgesCnt = 0;
		for (Node node : nodes)
			totalEdgesCnt += node.size();
		return totalEdgesCnt;
	}

	private List<Node> getPAPossibleNodes(Node inputNode) {
		List<Node> paPossibleNodes = new ArrayList<>();
		minusEdgesCnt = 0;
		for (Node node : nodes) {
			if (!node.contains(inputNode) && !node.equals(inputNode))
				paPossibleNodes.add(node);
			else if (!node.equals(inputNode))
				minusEdgesCnt += node.size();
		}
		return paPossibleNodes;
	}

	private float getRandomFloatPercentage() {
		return random.nextFloat() * 100;
	}

	private int getRandomEdgeCnt() {
		int rv = (int) (-2.0 * Math.log10(1 - random.nextFloat()));
		return rv + 1;
	}

	private int minusEdgesCnt;

	private int getPercentageIdx(List<Node> paPossibleNodes) {
		float[] percentages = new float[paPossibleNodes.size()];
		int totalEdgesCnt = getTotalEdgesCnt() - minusEdgesCnt;
		for (int i = 0; i < paPossibleNodes.size(); i++) {
			percentages[i] = (paPossibleNodes.get(i).size() + k / (float) paPossibleNodes.size()) * 100
					/ (totalEdgesCnt + k);
			if (i > 0)
				percentages[i] += percentages[i - 1];
		}
		float randomPercentage = getRandomFloatPercentage();
		for (int i = 0; i < percentages.length; i++)
			if (randomPercentage <= percentages[i])
				return i;
		return paPossibleNodes.size() - 1;
	}

	private int getRandomIdx(int size) {
		int rv = random.nextInt() % size;
		return rv < 0 ? -rv : rv;
	}
	
	private int linearThresholdCnt;
	public void doLinearThreshold() {
		List<Node> processList = new ArrayList<>();
		linearThresholdCnt = -1;
		do {
			linearThresholdCnt++;
			processList.clear();
			for(Node parent : nodes) {
				if(parent.hasData())	continue;
				int cnt = 0;
				for(Node child : parent.getEdges()) {
					if(child.hasData())	cnt++;
				}
				if((float)cnt/parent.size()>=0.5f)	processList.add(parent);
			}
			for(Node node : processList) {
				node.setData(true);
			}
			System.out.println("processList.size():"+processList.size());
		} while(!processList.isEmpty());
	}
	public int getLinearThresholdCnt() {
		return linearThresholdCnt;
	}
	public int getCntOfHasData() {
		int cnt = 0;
		for(Node node : nodes) 
			if(node.hasData())	cnt++;
		return cnt;
	}
}
