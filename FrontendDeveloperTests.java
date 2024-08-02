import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Contains methods to test the functionality of FrontendImplementation
 * NOTE: Every method is called as a result of the setup() method, and so methods will 
 * not be called within the tests themselves
 */
public class FrontendDeveloperTests extends ApplicationTest{
  
  /**
   * This method launches the JavaFX application that you would like to test
   * BeforeEach of your @Test methods are run.  Copy and paste this into your
   * own Test class, and change the SampleApp.class reference to instead
   * refer to your own application class that extends Application.
   */
  @BeforeEach
  public void setup() throws Exception {
  FrontendImplementation.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
  ApplicationTest.launch(FrontendImplementation.class); // CHANGE THIS TO IMPLEMENTATION LATER!
  } 
  
  /**
   * Test createAboutAndQuitControls and tests the use of times and via locations together
   */
  @Test
  public void Test1() {
    // get components for createAboutAndQuitControls
    Button about = lookup("#about").query();
    Label aboutLabel = lookup("#aboutlabel").query();
    Button quit = lookup("#quit").query();
    Assertions.assertEquals(about.getText(), "About");
    Assertions.assertEquals(quit.getText(), "Quit");
    // Tests that the about button correctly displays the controls if clicked
    Assertions.assertEquals(aboutLabel.getText(),"");
    clickOn("#about");
    Assertions.assertTrue(aboutLabel.getText().contains("Controls:"));
    
    // gets the path label
    Label first = lookup("#path").query();
    clickOn("#useviabox");
    clickOn("#ttbox");
    clickOn("#findbutton");
    
    // verifies that the output is what is expected
    Assertions.assertTrue(first.getText().contains("146"));
    Assertions.assertTrue(first.getText().contains("Radio Hall"));
    
  }
  /**
   * Test createShortestPathControls and createPathListDisplay
   */
  @Test
  public void Test2() {
    // Tests for createShortestPathControls
    // Looks up the components of the createShortestPathControls method
    Label first = lookup("#pathstartselector").query();
    Label second = lookup("#pathendselector").query();
    Button third = lookup("#findbutton").query();
    
    // Tests the functionality of the components from createShortestPathControls
    Assertions.assertTrue(first.getText().contains("Path Start Selector:"));
    Assertions.assertTrue(second.getText().contains("Path End Selector:"));
    Assertions.assertEquals(third.getText(),"Submit/Find Button");
    
    // Tests for createPathListDisplay
    clickOn("#findbutton");
    // looks up the fourth label where results should be
    Label fourth = lookup("#path").query();
    
    // tests the functionality of the button and label
    Assertions.assertTrue(fourth.getText().contains("Union South"));
    //Assertions.assertTrue(fourth.getText().contains("Results List (with travel times):"));  
  }
  /**
   * Tests createTravelTimesBox
   */
  @Test
  public void Test3() {
    // gets the components of createTravelTimeBox
    CheckBox first = lookup("#ttbox").query();
    Label second = lookup("#path").query();
    
    // (WILL FAIL FOR NOW!)
    // Tests that checking the box correctly adds the Results list with travel times
    clickOn("#findbutton");
    Assertions.assertTrue(!(second.getText().contains("Results List (with travel times):")));
    clickOn("#ttbox");
    clickOn("#findbutton");
    Assertions.assertTrue(second.getText().contains("Results List (with travel times):"));
  }
  /**
   * Test createOptionalLocationControls
   */
  @Test
  public void Test4() {
    // get components for createOptionalLocationControls
    CheckBox first = lookup("#useviabox").query();
    Label second = lookup("#locationselector").query();
    Label third = lookup("#path").query();
    
    clickOn("#useviabox");
    clickOn("#findbutton");
    // test the functionality of the components
    Assertions.assertTrue(second.getText().contains("Via Location (optional):"));
    Assertions.assertEquals(first.getText(), "Use Above Location in Path");
    Assertions.assertTrue(third.getText().contains("Radio Hall"));
  }
  
  // integration tests
  /**
   * Tests finding the shortest path without time and without via location
   */ 
  @Test
  public void IntegrationTest1() {
    // check to backend implementation and interface
    BackendInterface backend = new Backend(new DijkstraGraph<String,Double>());
    try {  
      // will need to change inside of VM
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {}
    
    FrontendImplementation.setBackend(backend);
    
    Label path = lookup("#path").query();
    
    // clicks on the path start text box and writes Union South
    write("Union South");
    // clicks on the path end text box and writes AOS Sciences
    clickOn("#endtext").write("Atmospheric, Oceanic and Space Sciences");
    // generates the shortest path
    clickOn("#findbutton");
    
    // verifies that the requires location appear in the Label
    Assertions.assertTrue(path.getText().contains("Union South"));
    Assertions.assertTrue(path.getText().contains("Atmospheric, Oceanic and Space Sciences")); 
  }
  /**
   * Tests finding the shortest path using time and via location
   */
  @Test
  public void IntegrationTest2() {
    // check to backend implementation and interface
    BackendInterface backend = new Backend(new DijkstraGraph<String,Double>());
    try {   
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {}
    FrontendImplementation.setBackend(backend);
    
    Label path = lookup("#path").query();
    
    // clicks on the path start text box and writes Union South
    write("Union South");    
    // clicks on the via location selector and writes Computer Sciences
    clickOn("#viatext").write("Computer Sciences and Statistics");   
    // clicks on the path end text box and writes AOS Sciences
    clickOn("#endtext").write("Atmospheric, Oceanic and Space Sciences");
    
    // sets the app to use both optional features
    clickOn("#useviabox");
    clickOn("#ttbox");
    
    // generates the shortest path
    clickOn("#findbutton");
    
    
    // verifies that the requires location appear in the Label
    Assertions.assertTrue(path.getText().contains("Union South"));
    Assertions.assertTrue(path.getText().contains("Computer Sciences and Statistics"));
    Assertions.assertTrue(path.getText().contains("Atmospheric, Oceanic and Space Sciences")); 
    Assertions.assertTrue(path.getText().contains("sec")); 
    Assertions.assertTrue(path.getText().contains("min")); 
  }
  
  // partner tests 
  
  /**
   * Test findShortestPath
   */
  @Test
  public void PartnerTest1() {
    // creates backend object with full implementations
    Backend backend = new Backend(new DijkstraGraph<String,Double>());
    try {   
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {}
    
    // gets results from find shortest path
    LinkedList<String> path = (LinkedList<String>) backend.findShortestPath("Memorial Union", "Union South");
    Assertions.assertTrue(path.contains("Memorial Union"));
    Assertions.assertTrue(path.contains("Union South"));
  }
  /**
   * Test getTravelTimesOnPath
   */
  @Test 
  public void PartnerTest2() {
    // creates backend object with full implementations
    Backend backend = new Backend(new DijkstraGraph<String,Double>());
    try {   
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {}
    
    ArrayList<Double> path = (ArrayList<Double>) backend.getTravelTimesOnPath("Memorial Union", "Science Hall");
   
    Assertions.assertTrue(path.contains(105.8));
  }
}
