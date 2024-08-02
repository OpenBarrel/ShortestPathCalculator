// === CS400 File Header Information ===
// Name: Diya Sriram
// Email: dsriram3@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: none

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {
	private GraphADT<String, Double> graph = new GraphPlaceholder();
	private List<String> nodes = new ArrayList<String>();

	/**
	 * Implementing classes should support the constructor below.
	 * 
	 * @param graph object to sture the backend's graph data
	 */
	public Backend(GraphADT<String, Double> graph) {
		this.graph = graph; // set the graph
	}

	/**
	 * Loads graph data from a dot file.
	 * 
	 * NOTE: THIS METHOD WAS EDITED BY NOAH BERG (FRONTEND)
	 * 
	 * @param filename the path to a dot file to read graph data from
	 * @throws IOException if there was a problem reading in the specified file
	 */
	public void loadGraphData(String filename) throws IOException {
	  
		File file = new File(filename);
    Scanner input;

    // make sure the file is valid
    try {
      input = new Scanner(file);
    } catch (Exception e) {
      throw new IOException("File not recognized");
    }
    // while there are lines to read
    while (input.hasNextLine()) {
    	String line = input.nextLine();
        String[] info = line.split("\"");
        
        // make sure it's the right length
        if (info.length != 5) // What is this?
          continue;
        
        // trims everything
        info[1].trim();
        info[3].trim();
        info[4].trim();
        
        // create double
        info[4] = info[4].substring(10, info[4].length() - 2);
        double value = Double.parseDouble(info[4]);
        
      // add the node, if it's not already in the graph
      if (!graph.containsNode(info[1])) { 
        graph.insertNode(info[1]);
        nodes.add(info[1]);
      }
      
      //////////// NOAH BERG //////////////
      
      // add the node, if it's not already in the graph
      if (!graph.containsNode(info[3])) { 
        graph.insertNode(info[3]);
        nodes.add(info[3]);
      }  
      
      //////////// END NOAH ///////////////
      
      // add the edge, if it's not already in the graph
      if (!graph.containsEdge(info[1], info[3]))
        graph.insertEdge(info[1], info[3], value); // What is info 2? Doesn't work
        // changing info[2] to info[3] in line 85 - NOAH
    }
    
    // close the scanner
    input.close();
	}

	/**
	 * Returns a list of all locations (nodes) available on the backend's graph.
	 * 
	 * @return list of all location names
	 */
	public List<String> getListOfAllLocations() {
		return this.nodes; // list containing all locations
	}

	/**
	 * Returns the sequence of locations along the shortest path from startLocation
	 * to endLocation, or en empty list if no such path exists.
	 * 
	 * @param startLocation the start location of the path
	 * @param endLocation   the end location of the path
	 * @return a list with the nodes along the shortest path from startLocation to
	 *         endLocation, or an empty list if no such path exists
	 */
	public List<String> findShortestPath(String startLocation, String endLocation) {
		return graph.shortestPathData(startLocation, endLocation); // shortest path
	}

	/**
	 * Returns the walking times in seconds between each two nodes on the shortest
	 * path from startLocation to endLocation, or an empty list of no such path
	 * exists.
	 * 
	 * @param startLocation the start location of the path
	 * @param endLocation   the end location of the path
	 * @return a list with the walking times in seconds between two nodes along the
	 *         shortest path from startLocation to endLocation, or an empty list if
	 *         no such path exists
	 */
	public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
		List<Double> times = new ArrayList<Double>();
		List<String> path = findShortestPath(startLocation, endLocation);

		// for each set of locations on the shortest path, get the cost(times in
		// seconds)
		// CHANGES path.size() - 2 to path.size() -1 - NOAH BERG
		for (int i = 0; i < path.size() - 1; i++) {
			times.add(graph.shortestPathCost(path.get(i), path.get(i + 1)));
		}

		return times;
	}

	/**
	 * Returns the sequence of locations along the shortest path from startLocation
	 * to endLocation including the third location viaLocation, or an empty list if
	 * no such path exists. en empty list if no such path exists.
	 * 
	 * @param startLocation the start location of the path
	 * @param viaLocation   a location that the path show lead through
	 * @param endLocation   the end location of the path
	 * @return a list with the nodes along the shortest path from startLocation to
	 *         endLocation including viaLocation, or an empty list if no such path
	 *         exists
	 */
	public List<String> findShortestPathVia(String startLocation, String viaLocation, String endLocation) {
		List<String> path1 = graph.shortestPathData(startLocation, viaLocation); // shortest path from start to via
		List<String> path2 = graph.shortestPathData(viaLocation, endLocation); // shortest path from via to end

		// add all the locations from the second path into the first path
		List<String> finalPath = new ArrayList<>(path1);
		finalPath.addAll(path2.subList(1, path2.size()));
				
		// CHANGED from path1 to finalPath by NOAH BERG
		return finalPath; // contains all locations
	}

	/**
	 * Returns the walking times in seconds between each two nodes on the shortest
	 * path from startLocation to endLocation through viaLocation, or an empty list
	 * of no such path exists.
	 * 
	 * @param startLocation the start location of the path
	 * @param viaLocation   a location that the path show lead through
	 * @param endLocation   the end location of the path
	 * @return a list with the walking times in seconds between two nodes along the
	 *         shortest path from startLocation to endLocationthrough viaLocation,
	 *         or an empty list if no such path exists
	 */
	public List<Double> getTravelTimesOnPathVia(String startLocation, String viaLocation, String endLocation) {
		List<String> path1 = findShortestPath(startLocation, viaLocation);
		List<String> path2 = findShortestPath(viaLocation, endLocation);
		List<Double> times = new ArrayList<Double>();
		
		// combine paths
		for (int i = 1; i < path2.size(); i++) {
			path1.add(path2.get(i));
		}

		// get the times
		for (int i = 0; i < path1.size()-1; i++)
			times.add(graph.getEdge(path1.get(i), path1.get(i + 1)));

		return times;
	}

}
