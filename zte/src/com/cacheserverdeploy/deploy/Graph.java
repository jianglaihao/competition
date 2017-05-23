package com.cacheserverdeploy.deploy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 邻接表表示图
 * 
 * @author 卡罗-晨
 */
public class Graph {

	private static int IMV = Integer.MAX_VALUE;
	int vertexsNum;// 顶点个数
	private int edgesNum;// 边数
	public VNode[] vertexs;// 邻接表
	VNode start;// 起点
	VNode end;// 终点
	VNode cornRoom;// 玉米间
	VNode fruitRoom;// 水果间
	Edge anteater;// 食蚁兽
	Edge[] gifts;// 神秘礼物
	VNode[] mustVertex; //必过顶点
	/**
	 * 邻接表-顶表
	 * @author 卡罗-晨
	 */
	public class VNode {
		int id;// 结点id
		public String info;// 结点信息
		ENode firstENode;// 指向第一个邻近点

		public VNode(int id, String info) {
			this.id = id;
			this.info = info;
		}
	}

	/**
	 * 邻接表-边表
	 * @author 卡罗-晨
	 */
	class ENode {
		int id;// 边结点id
		int weight;// 权重
		ENode nextENode;// 指向下一条边的指针

		public ENode(int id, int weight) {
			this.id = id;
			this.weight = weight;
		}
	}

	/**
	 * 图中的边
	 * @author 卡罗-晨
	 */
	class Edge {
		int start; // 边的起点
		int end; // 边的终点
		int weight; // 边的权重

		public Edge(int start, int end, int weight) {
			this.start = start;
			this.end = end;
			this.weight = weight;
		}
	}
	
	/**
	 * 
	 * @param graphContent
	 */
	public Graph(String[] graphContent) {
		Set<VNode> mustVertex = new HashSet<>();
		String[] strs = graphContent[0].split(" ");
		this.vertexsNum = Integer.parseInt(strs[0]);
		vertexs = new VNode[vertexsNum];
		this.edgesNum = Integer.parseInt(strs[1]);
		strs = graphContent[2].split(" ");
		for (int i = 0; i < vertexsNum; i++) {
			vertexs[i] = new VNode(i, strs[i]);
		}
		for (int i = 4; i < edgesNum + 4; i++) {
			strs = graphContent[i].split(" ");
			addVertex(strs[0], strs[1], Integer.parseInt(strs[2]));
			addVertex(strs[1], strs[0], Integer.parseInt(strs[2]));
		}
		gifts = new Edge[2];
		for (int i = edgesNum + 5; i < edgesNum + 7; i++) {
			strs = graphContent[i].split(" ");
			gifts[i - edgesNum - 5] = new Edge(getPosition(strs[0]), getPosition(strs[1]), Integer.parseInt(strs[2]));
			mustVertex.add(vertexs[getPosition(strs[0])]);
			mustVertex.add(vertexs[getPosition(strs[1])]);
		}
		strs = graphContent[edgesNum + 8].split(" ");
		this.anteater = new Edge(getPosition(strs[0]), getPosition(strs[1]), Integer.parseInt(strs[2]));
		updateWeight(getPosition(strs[0]),getPosition(strs[1]),IMV);
		updateWeight(getPosition(strs[1]),getPosition(strs[0]),IMV);
		this.cornRoom = vertexs[getPosition(graphContent[edgesNum + 10])];
		mustVertex.add(vertexs[getPosition(graphContent[edgesNum + 10])]);
		this.fruitRoom = vertexs[getPosition(graphContent[edgesNum + 11])];
		mustVertex.add(vertexs[getPosition(graphContent[edgesNum + 11])]);
		this.mustVertex = new VNode[mustVertex.size()];
		this.mustVertex = mustVertex.toArray(this.mustVertex);
		this.start = vertexs[getPosition(graphContent[edgesNum + 12])];
		this.end = vertexs[getPosition(graphContent[edgesNum + 13])];
	}
	
	/**
	 * 增加顶点
	 * @param startId
	 * @param endId
	 * @param weight
	 */
	public void addVertex(String startId, String endId, int weight) {
		int sid = getPosition(startId);
		int eid = getPosition(endId);
		ENode eNode = new ENode(eid, weight);
		if (vertexs[sid].firstENode == null) {
			vertexs[sid].firstENode = eNode;
		} else {
			eNode.nextENode = vertexs[sid].firstENode;
			vertexs[sid].firstENode = eNode;
		}
	}
	/**
	 * 更改权重
	 * @param startId
	 * @param endId
	 */
	private void updateWeight(int startId,int endId,int weight) {
		ENode feNode = vertexs[startId].firstENode;
		if(feNode.id == endId) {
			feNode.weight = weight;
		}else {
			ENode eNode = feNode.nextENode;
			while(eNode.id != endId) {
				eNode = eNode.nextENode;
			}
			eNode.weight = weight;
		}
	}
	/**
	 * 获取顶点信息在图数组中的位置
	 */
	private int getPosition(String info) {
		for (int i = 0; i < vertexs.length; i++)
			if (vertexs[i].info.equals(info))
				return i;
		return -1;
	}

	/**
	 * 深度优先搜索遍历，针对连通图的
	 * @param v
	 */
	public void DFS(int v, boolean[] visit) {
		visit[v] = true;
		// TODO visit()访问顶点的操作
		System.out.print(vertexs[v].info + " ");
		ENode e;
		e = vertexs[v].firstENode;
		while (e != null) {
			if (visit[e.id] == false) {
				DFS(e.id, visit);
			}
			e = e.nextENode;
		}
	}

	/**
	 * 广度优先搜索遍历，针对连通图的
	 * @param v
	 */
	public void BFS(int v) {
		Queue<Integer> queue = new LinkedList<>();// 需要一个队列保存以层次遍历的邻近点
		boolean[] visit = new boolean[vertexs.length];// 访问标志数组
		int i, j;
		// 数组被初始化为全未访问false
		for (i = 0; i < vertexs.length; i++) {
			visit[i] = false;
		}
		visit[v] = true;
		System.out.print(vertexs[v].info + " ");
		// TODO visit()
		queue.add(v);
		ENode e;
		while (!queue.isEmpty()) {
			j = queue.poll();// 队头结点出队
			e = vertexs[j].firstENode;// 队头结点j指向的第一条边
			while (e != null) {
				if (visit[e.id] == false) {
					// TODO visit()
					visit[e.id] = true;
					System.out.print(vertexs[e.id].info + " ");
					queue.add(e.id);
				}
				e = e.nextENode;
			}
		}
	}

	/**
	 * 获取边<start, end>的权值，若start和end不是相连的，则返回无穷大。
	 * @param start
	 * @param end
	 * @return
	 */
	public int getWeight(int start, int end) {
		if (start == end) {
			return 0;
		}
		ENode eNode = vertexs[start].firstENode;
		while (eNode != null) {
			if (eNode.id == end) {
				return eNode.weight;
			}
			eNode = eNode.nextENode;
		}
		return IMV;
	}

	/**
	 * 迪杰斯特拉算法，求出指定结点到剩余其他结点的最短路径
	 * @param v
	 */
	public int dijkstra(int v, int d, LinkedList<Integer> pathLink) {
		int[] dist = new int[vertexs.length];// dist[i]:结点v到结点i最短路径的长度
		int[] path = new int[vertexs.length];// path[i]:结点v到结点i的最短路径所经历的全部顶点中，位于"顶点i"之前的那个顶点
		boolean[] flag = new boolean[vertexs.length];// flag[i]=true：结点v到结点i的最短路径已成功获取。
		int min, i, j;
		// 初始化各个数组
		for (i = 0; i < vertexs.length; i++) {
			dist[i] = getWeight(v, i);
			flag[i] = false;
			if (getWeight(v, i) < IMV) {
				path[i] = v;
			} else
				path[i] = -1;
		}
		flag[v] = true;
		path[v] = -1;
		int u = 0;
		
		//// 遍历mVexs.length-1次；每次找出一个顶点的最短路径
		for (i = 1; i < vertexs.length; i++) {
			min = IMV;
			// 这个循环每次从剩余结点中选出一个结点，经过这个结点再通往所有剩余结点的路径中是最短的
			for (j = 0; j < vertexs.length; j++) {
				if (flag[j] == false && dist[j] < min) {
					u = j;
					min = dist[j];
				}
			}
			flag[u] = true;
			
			if(vertexs[u].id == d){
				pathLink.add(d);
				int o = d;
				while(o != v) {
					pathLink.add(path[o]);
					o = path[o];
				}
				return dist[u];
			}
			
			// 这个循环以刚并入的结点作为中间点，对所有通过剩余顶点的路径进行检测
			for (j = 0; j < vertexs.length; j++) {
				int tmp = getWeight(u, j);
				tmp = (tmp == IMV ? IMV : min + tmp);
				// 这个if语句判断结点u的加入是否会出现通过结点j的更短的路径，如果出现，则改变原来的路径及长度，否则什么都不做
				if (flag[j] == false && tmp < dist[j]) {
					dist[j] = tmp;
					path[j] = u;
				}
			}
		}
		return dist[u];
	}

	/**
	 * 邻接表打印图
	 */
	public void print() {
		System.out.printf("List Graph:\n");
		for (int i = 0; i < vertexs.length; i++) {
			System.out.printf("%d(%s): ", i, vertexs[i].info);
			ENode node = vertexs[i].firstENode;
			while (node != null) {
				System.out.printf("%d(%s) ", node.id, vertexs[node.id].info);
				node = node.nextENode;
			}
			System.out.printf("\n");
		}
	}

}
