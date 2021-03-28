package randomsocialnetworkservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import socialnetworkservice.Node;

public class Network {
	private List<Node> nodes = new ArrayList<>();
	private float percentage = 0.01f; // 0.06%(퍼센트) -> 0.0006(1/100)
	private int nodeCnt = 5000;

	public Network() {
		Node[] nodes = new Node[nodeCnt];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i + 1);
		}

		for (int i = 0; i < nodes.length; i++) {

			if (i == (nodes.length - 1)) {
				System.out.println("초기설정 완료");
			} else {
				nodes[i].addEdge(nodes[i + 1]);
				nodes[i + 1].addEdge(nodes[i]);
			}
		}

		Random random = new Random();
		int tmp;
		for (Node owner : nodes) {
			for (Node guest : nodes) {
				if (!owner.equals(guest) && !guest.contains(owner) && !owner.contains(guest)
						&& random.nextFloat() < percentage / 100f) {
					owner.addEdge(guest);
					guest.addEdge(owner);
				}
			}
			if (owner.getEdges().size() == 0) {
				do {
					tmp = random.nextInt();
					tmp = tmp < 0 ? -tmp % nodeCnt : tmp % nodeCnt;
				} while (nodes[tmp].equals(owner));
				owner.addEdge(nodes[tmp]);
				nodes[tmp].addEdge(owner);
			}
		}
		for (Node node : nodes) {
			this.nodes.add(node);
		}

	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public int getNodeCnt() {
		return nodeCnt;
	}

	public void setNodeCnt(int nodeCnt) {
		this.nodeCnt = nodeCnt;
	}

	public float getPercentage() {
		return percentage;
	}

	public List<Node> getNodes() {
		return nodes;
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
		// System.out.println(cascadeNodes.size());
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

	public void printHasData() {
		for (Node n : nodes) {
			if (n.hasData())
				System.out.print(n.getValue() + ", ");
		}
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


