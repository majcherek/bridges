package pl.edu.agh.io.bridges;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		
		if(args.length < 2){
			System.out.println("Usage: <tgf file path> <root node name>");
			return;
		}
		
		Graph graph = initGraph(args[0]);		

		List<Edge> bridges = new BridgesUtils().getBridges(graph.getNode(args[1]));
		System.out.println("Bridges:");
		bridges.forEach(System.out::println);
		
		System.out.println("\nDFS:");
		graph.getEachEdge().forEach(e -> {
			if((boolean)e.getAttribute(BridgesUtils.SPANNING_TREE)){
				System.out.println(e);
			}
		});

	}
	
	private static Graph initGraph(String filePath) throws IOException{
		Graph graph = new SingleGraph("io");
	    
	    BufferedReader br = new BufferedReader(new FileReader(filePath));
	    String line;
	    while(!(line = br.readLine()).contains("#")){
	    	Node n = graph.addNode(line.split(" ")[0]);
	    	n.addAttribute(BridgesUtils.VISITED, false);
	    }
	    while( (line = br.readLine()) != null){

	    	String[] edgeParams = line.split(" ");
			Edge e = graph.addEdge(edgeParams[2], edgeParams[0], edgeParams[1]);
			e.addAttribute(BridgesUtils.SPANNING_TREE, false);
	    }
	    
	    br.close();
		
		return graph;
	}

	
}
