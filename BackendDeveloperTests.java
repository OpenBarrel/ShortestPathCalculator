// === CS400 File Header Information ===
// Name: Diya Sriram
// Email: dsriram3@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: none

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackendDeveloperTests {

	/**
	 * Tests that loadGraphData() loads data in properly and throws an exception
	 * when needed.
	 */
	@Test
	public void test1Rolecode() throws IOException {
		GraphADT<String, Double> graph = new GraphPlaceholder();
		Backend backend = new Backend(graph);

		// test for exception
		backend.loadGraphData("./campus.dot"); 
    Assertions.assertThrows(IOException.class, () -> backend.loadGraphData("wrong name"),
        "Expected Exception not thrown");
		
    // checks that everything is loaded properly
    Assertions.assertTrue(graph.containsEdge("Union South", "Computer Sciences and Statistics"));
    Assertions.assertEquals(176.0, graph.getEdge("Union South", "Computer Sciences and Statistics"));
	}

	/**
	 * Tests that getListOfAllLocations() returns a list of all locations(nodes) on
	 * the backend's graph
	 */
	@Test
	public void test2Rolecode() throws IOException{
    GraphADT<String, Double> graph = new GraphPlaceholder();
    Backend backend = new Backend(graph);
    
    // should be empty
    ArrayList<String> expected = new ArrayList<String>();
    Assertions.assertEquals(expected, backend.getListOfAllLocations());
    
    backend.loadGraphData("./campus.dot");
    
    // check for random locations
    Assertions.assertTrue(backend.getListOfAllLocations().contains("Davis Residence Hall"),
        "Missing Valid Location");
    Assertions.assertTrue(!backend.getListOfAllLocations().contains("This does not exist"),
        "Includes Invalid Location");
	}

	/**
	 * Tests that findShortestPath() returns the sequence of locations along the
	 * shortest path from startLocation to endLocation and an empty list if no such
	 * path exists. Tests that getTravelTimesOnPath() returns the walking times in
	 * seconds between each two nodes on the shortest path from startLocation to
	 * endLocation and an empty list of no such path exists.
	 */
	@Test
	public void test3Rolecode() {
    GraphADT<String, Double> graph = new GraphPlaceholder();
    Backend backend = new Backend(graph);
    
    ArrayList<String> expected = new ArrayList<String>();
    expected.add("Union South");
    expected.add("Computer Sciences and Statistics");
    expected.add("Atmospheric, Oceanic and Space Sciences");
    String start = "Union South";
    String end = "Atmospheric, Oceanic and Space Sciences";

    // checks path
    Assertions.assertEquals(expected, backend.findShortestPath(start, end));
	}

	/**
	 * Tests that findShortestPathVia() returns the sequence of locations along the
	 * shortest path from startLocation to endLocation and an empty list if no such
	 * path exists. Tests that getTravelTimesOnPathVia() returns the walking times
	 * in seconds between each two nodes on the shortest path from startLocation to
	 * endLocation and an empty list of no such path exists.
	 */
	@Test
	public void test4Rolecode() {
    GraphADT<String, Double> graph = new GraphPlaceholder();
    Backend backend = new Backend(graph);
    String start = "Union South";
    String mid = "Computer Sciences and Statistics";
    String end = "Atmospheric, Oceanic and Space Sciences";
    
    // correct outcomes
    ArrayList<Double> expectedTimes = new ArrayList<Double>();
    ArrayList<String> expectedPath = new ArrayList<String>();
    expectedTimes.add(176.0);
    expectedTimes.add(127.2);
    expectedPath.add("Union South");
    expectedPath.add("Computer Sciences and Statistics");
    expectedPath.add("Atmospheric, Oceanic and Space Sciences");

    // checks both Via paths
    Assertions.assertEquals(expectedPath, backend.findShortestPathVia(start, mid, end));
    Assertions.assertEquals(expectedTimes, backend.getTravelTimesOnPathVia(start, mid, end));
	}

	// All placeholder tests that were submitted for the midweek

	/**
	 * Tests that loadGraphData() loads data in properly and throws an exception
	 * when needed.
	 */
	@Test
	public void test1Placeholder() {
		BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());
		// correctly loads in data
		assertDoesNotThrow(() -> backend.loadGraphData("campus.dot"));

		// incorrectly loads in data and throws an exception
		assertThrows(IOException.class, () -> backend.loadGraphData("campu.dot"));

	}

	/**
	 * Tests that getListOfAllLocations() returns a list of all locations(nodes) on
	 * the backend's graph
	 */
	@Test
	public void test2Placeholder() {
		BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());

		// list of all locations
		List<String> actual = backend.getListOfAllLocations();

		// make sure all locations are there
		assertTrue(!actual.isEmpty());
		assertTrue(actual.contains("Union South"));
		assertTrue(actual.contains("Computer Sciences and Statistics"));
		assertTrue(actual.contains("Atmospheric, Oceanic and Space Sciences"));
	}

	/**
	 * Tests that findShortestPath() returns the sequence of locations along the
	 * shortest path from startLocation to endLocation and an empty list if no such
	 * path exists. Tests that getTravelTimesOnPath() returns the walking times in
	 * seconds between each two nodes on the shortest path from startLocation to
	 * endLocation and an empty list of no such path exists.
	 */
	@Test
	public void test3Placeholder() {
		BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());

		// get lists
		String start = "Union South";
		String end = "Atmospheric, Oceanic and Space Sciences";
		List<String> path = backend.findShortestPath(start, end);
		List<Double> times = backend.getTravelTimesOnPath(start, end);

		// check path
		assertTrue(!path.isEmpty());
		assertEquals(3, path.size());
		assertEquals("Union South", path.get(0));
		assertEquals("Computer Sciences and Statistics", path.get(1));
		assertEquals("Atmospheric, Oceanic and Space Sciences", path.get(2));

		// check times
		assertTrue(!times.isEmpty());
		assertEquals(2, times.size());
		assertEquals(176.0, times.get(0));
		assertEquals(80.0, times.get(1));

		// empty list if no path exists
		List<String> emptyPath = backend.findShortestPath("a", end);
		List<Double> emptyTimes = backend.getTravelTimesOnPath("a", end);
		assertTrue(emptyPath.isEmpty());
		assertTrue(emptyTimes.isEmpty());

	}

	/**
	 * Tests that findShortestPathVia() returns the sequence of locations along the
	 * shortest path from startLocation to endLocation and an empty list if no such
	 * path exists. Tests that getTravelTimesOnPathVia() returns the walking times
	 * in seconds between each two nodes on the shortest path from startLocation to
	 * endLocation and an empty list of no such path exists.
	 */
	@Test
	public void test4Placeholder() {
		BackendInterface backend = new BackendPlaceholder(new GraphPlaceholder());

		// get lists
		String start = "Memorial Union";
		String via = "Science Hall";
		String end = "Radio Hall";
		List<String> path = backend.findShortestPathVia(start, via, end);
		List<Double> times = backend.getTravelTimesOnPathVia(start, via, end);

		// check path
		assertTrue(!path.isEmpty());
		assertEquals(3, path.size());
		assertEquals("Memorial Union", path.get(0));
		assertEquals("Science Hall", path.get(1));
		assertEquals("Radio Hall", path.get(2));

		// check times
		assertTrue(!times.isEmpty());
		assertEquals(2, times.size());
		assertEquals(146.0, times.get(0));
		assertEquals(30.0, times.get(1));

		// empty list if no path exists
		List<String> emptyPath = backend.findShortestPath("a", end);
		List<Double> emptyTimes = backend.getTravelTimesOnPath("a", end);
		assertTrue(emptyPath.isEmpty());
		assertTrue(emptyTimes.isEmpty());

	}

}
