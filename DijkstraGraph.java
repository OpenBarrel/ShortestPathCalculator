// === CS400 File Header Information ===
// Name: <your full name>
// Email: <your @wisc.edu email address>
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: <name of your lecturer>
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
      // changed to HashtableMap
        super(new HashtableMap<>()); // Testing
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
      // implement in step 5.3
      
      // settled set - map
      MapADT<NodeType,SearchNode> settled = new HashtableMap<NodeType,SearchNode>();
      // unsettled set - PQ
      PriorityQueue<SearchNode> unsettled = new PriorityQueue<SearchNode>();
      // adds the starting node to the unsettled set with a cost of 0
      unsettled.add(new SearchNode(nodes.get(start),0.0,null));
      
      while (unsettled.isEmpty() == false) {
        // gets and removes the top node from the PQ
        SearchNode temp = unsettled.remove();         
        
        // if the settled set does not contain this node
        if (!(settled.containsKey(temp.node.data))) {
          // add node to the settled set
          settled.put(temp.node.data, temp);
          // update costs to neighbors and add new ones
          
          for (Edge edge : temp.node.edgesLeaving) {
            // if the connected node is not in settled, add it
            if (!(settled.containsKey(edge.successor.data))) {
              // if the connected node is not in the unsettled, add it
              if (!(unsettled.contains(edge.successor.data)))
                unsettled.add(new SearchNode(edge.successor,((Double)edge.data.doubleValue() + 
                    temp.cost),temp));
              // if the connected node is already in unsettled, update it
              else {
                // creates varaibles for the current and new potential cost 
                double currCost = settled.get(edge.successor.data).cost;
                double newCost = temp.cost + (Double)edge.data.doubleValue();
                // if the new cost is less than the current cost
                if (newCost < currCost)
                  unsettled.add(new SearchNode(edge.successor,newCost,temp));     
              }
            }   
          } 
        }
      }      
        // return the end node if it exists, get will automatically throw a NoSuchElement exception
        // if end is not presents
        return settled.get(end);
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // implement in step 5.4
      SearchNode result = computeShortestPath(start,end);
      List<NodeType> list = new LinkedList<NodeType>();
      
      while (result != null) {
        list.add(0,result.node.data);
        result = result.predecessor;
      }  
        return list;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return computeShortestPath(start,end).cost;
    }

    // TODO: implement 3+ tests in step 4.1
    
    /**
     * Tests that the method correctly finds the shortest path
     */
	/*
    @Test
    public void test1() {
      // creates a tester object
      DijkstraGraph<Character,Integer> tester = new DijkstraGraph<Character, Integer>();
      // insert nodes
      tester.insertNode('a');
      tester.insertNode('b');
      tester.insertNode('c');
      tester.insertNode('d');
      tester.insertNode('e');
      tester.insertNode('f');
      tester.insertNode('g');
      // create edges
      tester.insertEdge('a', 'f', 1);
      tester.insertEdge('a', 'd', 3);
      tester.insertEdge('a', 'b', 5);
      tester.insertEdge('b', 'c', 4);
      tester.insertEdge('c', 'g', 4);
      tester.insertEdge('c', 'd', 3);
      tester.insertEdge('c', 'e', 2);
      tester.insertEdge('d', 'b', 5);
      tester.insertEdge('d', 'f', 1);
      tester.insertEdge('e', 'd', 3);
      tester.insertEdge('e', 'g', 2);
      tester.insertEdge('f', 'e', 4);
      tester.insertEdge('g', 'b', 2);
      tester.insertEdge('g', 'f', 5);
      
      // uses the computeShortestPath method
      DijkstraGraph<Character, Integer>.SearchNode result = tester.computeShortestPath('a', 'g');
      // checks that all of the fields are correct

      Assertions.assertEquals(result.node.data.equals('g'),true);
      Assertions.assertEquals(result.predecessor.node.data.equals('e'), true);
      Assertions.assertEquals(result.predecessor.predecessor.node.data.equals('f'), true);
      Assertions.assertEquals(result.predecessor.predecessor.predecessor.node.data.equals('a'), true);
    }
*/
    /**
     * Tests that the cost and sequence of data along the shortest path is correct
     */
/*  
  @Test
    public void test2() {
      // creates a tester object
      DijkstraGraph<Character,Integer> tester = new DijkstraGraph<Character, Integer>();
      // insert nodes
      tester.insertNode('a');
      tester.insertNode('b');
      tester.insertNode('c');
      tester.insertNode('d');
      tester.insertNode('e');
      tester.insertNode('f');
      tester.insertNode('g');
      // create edges
      tester.insertEdge('a', 'f', 1);
      tester.insertEdge('a', 'd', 3);
      tester.insertEdge('a', 'b', 5);
      tester.insertEdge('b', 'c', 4);
      tester.insertEdge('c', 'g', 4);
      tester.insertEdge('c', 'd', 3);
      tester.insertEdge('c', 'e', 2);
      tester.insertEdge('d', 'b', 5);
      tester.insertEdge('d', 'f', 1);
      tester.insertEdge('e', 'd', 3);
      tester.insertEdge('e', 'g', 2);
      tester.insertEdge('f', 'e', 4);
      tester.insertEdge('g', 'b', 2);
      tester.insertEdge('g', 'f', 5); 
      
      // use and store results of the two methods
      double cost = tester.shortestPathCost('a', 'g');
      List<Character> list = tester.shortestPathData('a', 'g');
      // check results
      Assertions.assertEquals(cost == 7.0, true);
      Assertions.assertEquals(list.contains('a'), true);
      Assertions.assertEquals(list.contains('f'), true);
      Assertions.assertEquals(list.contains('e'), true);
      Assertions.assertEquals(list.contains('g'), true);
    }
*/
  /**
     * Tests behavior when no valid connection between two nodes exists (path is impossible)
     */
/*  
  @Test
    public void test3() {
      // creates a tester object
      DijkstraGraph<Character,Integer> tester = new DijkstraGraph<Character, Integer>();
      // insert nodes
      tester.insertNode('a');
      tester.insertNode('b');
      tester.insertNode('c');
      tester.insertNode('d');
      tester.insertNode('e');
      tester.insertNode('f');
      tester.insertNode('g');
      // insert edges (removed all connections to g)
      tester.insertEdge('a', 'f', 1);
      tester.insertEdge('a', 'd', 3);
      tester.insertEdge('a', 'b', 5);
      tester.insertEdge('b', 'c', 4);
      tester.insertEdge('c', 'd', 3);
      tester.insertEdge('c', 'e', 2);
      tester.insertEdge('d', 'b', 5);
      tester.insertEdge('d', 'f', 1);
      tester.insertEdge('e', 'd', 3);
      tester.insertEdge('f', 'e', 4);
      tester.insertEdge('g', 'b', 2);
      tester.insertEdge('g', 'f', 5);   
      // tests that calling these methods throws the proper exception
      try {
        tester.computeShortestPath('a', 'g');
        Assertions.fail();
      } catch (NoSuchElementException nee) {} 
      try {
        tester.shortestPathCost('a', 'g');
        Assertions.fail();
      } catch (NoSuchElementException nee) {} 
      try {
        tester.shortestPathData('a', 'g');
        Assertions.fail();
      } catch (NoSuchElementException nee) {} 
    }
*/
}
