package com.cacheserverdeploy.deploy;

import java.util.LinkedList;

public class Deploy {
	/**
	 * <功能详细描述>
	 * @param graphContent
	 *            用例信息文件
	 * @return [参数说明] 输出结果信息
	 */
	public static String[] deployServer(String[] graphContent) {
		/** do your work here **/
		Graph graph = new Graph(graphContent);
		
		FinalPath finalPath = new FinalPath(graph);
		LinkedList<Integer> list = new LinkedList<Integer>();
		int dis = finalPath.deploy(list);
		
//		System.out.println(dis);
		StringBuilder sb = new StringBuilder();
		int vnodeNum = list.size();
		while(!list.isEmpty()) {
			int p = list.removeLast();
			sb.append(graph.vertexs[p].info+ " ");
//			System.out.print(graph.vertexs[p].info+" ");
			
	}
		
		return new String[] { dis+"", vnodeNum+"", sb.toString() };
	}

}
