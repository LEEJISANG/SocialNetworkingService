package randomsocialnetworkservice;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Scanner;

import socialnetworkservice.Node;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Network network = new Network();
		
		/*
		 * int edgeCnt = 0;
		 * 
		 * for (Node node : network.getNodes()) { System.out.print(node.getValue() +
		 * " edges : "); for (Node edge : node.getEdges()) {
		 * System.out.print(edge.getValue() + ", "); } System.out.println(); edgeCnt +=
		 * node.getEdges().size(); } System.out.println("총 엣지수 : " + edgeCnt);
		 * System.out.println("엣지 평균 : " + (float) edgeCnt / network.getNodeCnt());
		 */

		File file = new File("./output.csv");
		try {
			FileWriter w = new FileWriter(file, false);
			for (Node node : network.getNodes()) {
				for (Node edge : node.getEdges())
					w.write(node.getValue() + ", " + edge.getValue() + "\r\n");
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file2 = new File("./output2.txt");
		try {
			FileWriter w = new FileWriter(file2, false);
			Map<Integer, Integer> map = network.getEdgeNodePair();
			for (int key : map.keySet())
				w.write(key + ", " + map.get(key) + "\r\n");
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Linear Threshold Model
		int avg = 0;
		int[] idxes = null;
		int reCnt = 50;
		String[] models = new String[] { "Degree", "Betweenness", "Closeness", "Eigenvector", "Random"};
		for (String model : models) {
			avg = 0;
			System.out.println("----LT model of " + model + " Centrality------");
			System.out.print("The number of nodes that have initial information : ");
			idxes = new int[sc.nextInt()];
			System.out.print("Enter node number selected by Degree Centrality : ");
			for (int i = 0; i < idxes.length; i++) {
				idxes[i] = sc.nextInt() - 1;
			}
			for (int re = 0; re < reCnt; re++) {
				for (Integer idx : idxes)
					network.getNodes().get(idx).setData(true);
				network.doLinearThreshold();
				System.out.println("Linear Threshold spread execution count" + (re + 1) + " : Number "
						+ network.getLinearThresholdCnt());
				avg += network.getLinearThresholdCnt();
				network.initHasData();
			}
			System.out.println("Linear Threshold spread execution average : " + avg / (float) reCnt);
		}

		// Cascade Threshold Model
//		models = new String[] { "Degree", "Betweenness", "Closeness", "Eigenvector", "Random" };
//		for (String model : models) {
//			avg = 0;
//			System.out.println("----IC model of " + model + " Centrality------");
//			System.out.print("The number of nodes that have initial information : ");
//			idxes = new int[sc.nextInt()];
//			System.out.print("Enter node number selected by " + model + " Centrality : ");
//			for (int i = 0; i < idxes.length; i++) {
//				idxes[i] = sc.nextInt() - 1;
//			}
//			for (int re = 0; re < reCnt; re++) {
//				for (Integer idx : idxes)
//					network.getNodes().get(idx).setData(true);
//				while (!network.hasAllNodesData()) {
//					network.doCascadeSpread();
//				}
//				System.out.println("Cascade Threshold spread execution count" + (re + 1) + " : Number "
//						+ network.getCascadeSpreadCnt());
//				avg += network.getCascadeSpreadCnt();
//				network.initHasData();
//				network.reSetCascadeSpread();
//			}
//			System.out.println("Cascade Threshold spread execution average : " + avg / (float) reCnt);
//		}
	}
}