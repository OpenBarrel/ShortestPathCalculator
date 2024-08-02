// === CS400 File Header Information ===
// Name: Diya Sriram
// Email: dsriram3@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: none


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GraphPlaceholder implements GraphADT<String, Double> {

  protected List<String> path;

  public GraphPlaceholder() {
    path = new ArrayList<>();
    path.add("Union South");
    path.add("Computer Sciences and Statistics");
    path.add("Atmospheric, Oceanic and Space Sciences");
  }

  public boolean insertNode(String data) {
    return false;
  }

  public boolean removeNode(String data) {
    return false;
  }

  public boolean containsNode(String data) {
    return path.contains(data);
  }

  public int getNodeCount() {
    return path.size();
  }

  public boolean insertEdge(String pred, String succ, Double weight) {
    return false;
  }

  public boolean removeEdge(String pred, String succ) {
    return false;
  }

  public boolean containsEdge(String pred, String succ) {
    return (pred.equals("Union South") && succ.equals("Computer Sciences and Statistics"))
        || (pred.equals("Computer Sciences and Statistics")
            && pred.equals("Atmospheric, Oceanic and Space Sciences"));
  }

  public Double getEdge(String pred, String succ) {
    if (pred.equals("Union South") && succ.equals("Computer Sciences and Statistics")) {
      return 176.0;
    } else if (pred.equals("Computer Sciences and Statistics")
        && succ.equals("Atmospheric, Oceanic and Space Sciences")) {
      return 127.2;
    } else {
      throw new NoSuchElementException();
    }
  }

  public int getEdgeCount() {
    return path.size() - 1;
  }

  public List<String> shortestPathData(String start, String end) {
      if (start.equals("Union South") && end.equals("Computer Sciences and Statistics")) {
        path = new ArrayList<>();
        path.add("Union South");
        path.add("Computer Sciences and Statistics");
        return path;
      } else if (start.equals("Computer Sciences and Statistics")
          && end.equals("Atmospheric, Oceanic and Space Sciences")) {
        path = new ArrayList<>();
        path.add("Computer Sciences and Statistics");
        path.add("Atmospheric, Oceanic and Space Sciences");
        return path;
      } else if (start.equals("Union South")
          && end.equals("Atmospheric, Oceanic and Space Sciences")){
        path = new ArrayList<>();
        path.add("Union South");
        path.add("Computer Sciences and Statistics");
        path.add("Atmospheric, Oceanic and Space Sciences");
        return path;
      }  else {
        throw new NoSuchElementException();
      }
  }

  public double shortestPathCost(String start, String end) {
    return 303.2;
  }
}
