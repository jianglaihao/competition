package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cacheserverdeploy.deploy.Graph.ENode;
import com.cacheserverdeploy.deploy.Graph.VNode;

public class FinalPath {
	Graph graph;
	List<VNode[]> fullArr = new ArrayList<>();

	public FinalPath(Graph graph) {
		this.graph = graph;
	}

	private void swap(VNode[] vs, int i1, int i2) {
		VNode temp = vs[i2];
		vs[i2] = vs[i1];
		vs[i1] = temp;
	}

	public void fullArr(VNode[] vs, int begin, int end) {
		if (end == begin) { // 一到递归的出口就输出数组，此数组为全排列
			VNode[] vnodes = new VNode[vs.length];
			for (int i = 0; i <= end; i++) {
				// System.out.print(vs[i].info+" ");
				vnodes[i] = vs[i];
			}
			fullArr.add(vnodes);
			// System.out.println();
			return;
		} else {
			for (int j = begin; j <= end; j++) {
				swap(vs, begin, j); // for循环将begin~end中的每个数放到begin位置中去
				fullArr(vs, begin + 1, end); // 假设begin位置确定，那么对begin+1~end中的数继续递归
				swap(vs, begin, j); // 换过去后再还原
			}
		}
	}

	public int deploy(LinkedList<Integer> list) {
		fullArr(graph.mustVertex, 0, graph.mustVertex.length - 1);
		int path = Integer.MAX_VALUE;// 最终最短路径距离
		int midPath;
		 
		// 遍历中间节点集
		for (VNode[] vs : fullArr) {
			if(check(vs)) {
				LinkedList<Integer> oneTimeList = new LinkedList<>();
				int temp = graph.dijkstra(graph.start.id, vs[0].id, oneTimeList);// 源点到中间点集的距离
				midPath = pathMidNode(oneTimeList,vs);
				
				LinkedList<Integer> list2 = new LinkedList<>();
				temp += graph.dijkstra(vs[vs.length - 1].id, graph.end.id, list2) + midPath;
				for(int j =list2.size()-2;j>=0;j--){
					oneTimeList.addFirst(list2.get(j));
				}
				
				if (temp < path) {
					path = temp;
					list.clear();
					list.addAll(oneTimeList);
				}
			}
			
		}
		return path;
	}

	private int pathMidNode(LinkedList<Integer> list, VNode[] vs) {
		int dist =0;
		for (int i = 0; i < vs.length-1; i++) {
			int t = Integer.MAX_VALUE;
			ENode eNode = graph.vertexs[vs[i].id].firstENode;
			while (eNode != null) {
				if (eNode.id == vs[i+1].id) {
					t = eNode.weight;
					list.addFirst(vs[i+1].id);
				}
				eNode = eNode.nextENode;
			}
			if(t == Integer.MAX_VALUE){
				LinkedList<Integer> list2 = new LinkedList<>();
				t = graph.dijkstra(vs[i].id, vs[i+1].id, list2);
				for(int j =list2.size()-2;j>=0;j--){
					list.addFirst(list2.get(j));
				}
			}
			dist +=t;
		}
		return dist;
	}

	private boolean check(VNode[] vs) {
		int t=0,k=0;
		for (int i = 0;i< vs.length-1 ;i++) {
			if(vs[i].id == graph.gifts[0].start) {
				if(vs[i+1].id == graph.gifts[0].end) {
					t++;
				}
			}
			if(vs[i].id == graph.gifts[0].end) {
				if(vs[i+1].id == graph.gifts[0].start) {
					t = 1;
				}
			}
			if(vs[i].id == graph.gifts[1].start) {
				if(vs[i+1].id == graph.gifts[1].end) {
					k++;
				}
			}
			if(vs[i].id == graph.gifts[1].end) {
				if(vs[i+1].id == graph.gifts[1].start) {
					k = 1;
				}
			}
			if(t+k==2){
				return true;
			}
		}
		return false;
	}

}
