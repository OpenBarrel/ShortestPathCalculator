import javafx.scene.layout.Pane;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.text.DecimalFormat;  

public class FrontendImplementation extends Application implements FrontendInterface{
  // static varaible for a BackendInterface type object
  private static BackendInterface back;
  private boolean showTimes = false;
  private boolean showAbout = false;
  private boolean useViaLocation = false;
  
  /**
   * Use to set the Backend object for the Frontend
   * @param back the BackendInterface type object
   */
  public static void setBackend(BackendInterface back) {
    FrontendImplementation.back = back;
  }

  /**
   * Configures the window and calls the createAllControls method to make the UI
   */
  public void start(Stage stage) {
    Pane root = new Pane();

    createAllControls(root);
    
    Scene scene = new Scene(root, 800, 600);
    
    stage.setScene(scene);
    stage.setTitle("P2: Prototype");
    stage.show();
  }
  
  /**
   * Creates all controls in the GUI.
   * @param parent the parent pane that contains all controls
   */
  @Override
  public void createAllControls(Pane parent) {
        
    createShortestPathControls(parent);
    createPathListDisplay(parent);
    createAdditionalFeatureControls(parent);
    createAboutAndQuitControls(parent);  
  }
  
  /**
   * Helper method to generate the string for the path label when even the find path button is used
   * @param start the location to start from
   * @param via the location we'd like to visit in the middle
   * @param end the destination
   * @return a string that lists these locations (also sometimes times) in proper format 
   */
  private String getPathLabel(String start, String via, String end) {
    // lists to store string results and times
    List<String> results;
    List<Double> times;
    String toReturn = "";
    DecimalFormat df = new DecimalFormat("0.00");
        
    // checks if the showTimes button is checked
    if (showTimes == true) {
      toReturn += "Results List (with travel times):";
      // checks if the useViaLocation button is checked
      if (useViaLocation == true) {
        // stores the results from backend
        results = back.findShortestPathVia(start, via, end);
        times = back.getTravelTimesOnPathVia(start, via, end);   
        
        // uses a loop to add all of the locations and their times to the return string
        double sum = 0.0;
        toReturn += "\n\t"+results.get(0);
        for (int i = 1; i < results.size(); i++) {
          toReturn += "\n\t-(" + (Integer)times.get(i-1).intValue() + "sec)->"+results.get(i);
          sum += times.get(i-1);
        }
        // adds the total time to the end
        toReturn += "\n\tTotal time: " + df.format(sum/60) + "min";
        
      } else { // ERROR IN THIS PART OF THE CODE
        // stores the results from backend
        results = back.findShortestPath(start, end);
        times = back.getTravelTimesOnPath(start, end);
        
        // uses a loop to add all of the locations and their times to the return string
        double sum = 0.0;
        toReturn += "\n\t"+results.get(0);
        for (int i = 1; i < results.size(); i++) {
          toReturn += "\n\t-(" + (Integer)times.get(i-1).intValue() + "sec)->"+results.get(i);
          sum += times.get(i-1);
        }
        // adds the total time to the end
        toReturn += "\n\tTotal time: " + df.format(sum/60) + "min";
      }
    } else {
      toReturn += "Results List: ";
      // checks if the useViaLocation button is checked
      if (useViaLocation == true) {
        // stores results from backend
        results = back.findShortestPathVia(start, via, end);
        // uses a loop to add all locations to the return string
        for (String s : results) {
          toReturn += "\n\t" + s;
        }
      } else {
        // stores results from backend
        results = back.findShortestPath(start, end);
        for (String s : results) {
          // uses a loop to add all locations to the return string
          toReturn += "\n\t" + s;
        }
      }
    }
    return toReturn;
  }
  
  
  /**
   * Creates the controls for the shortest path search.
   * @param parent the parent pane that contains all controls
   */
  @Override
  public void createShortestPathControls(Pane parent) {
    // creates GridPane to store all elements and create elements for start selector
    GridPane grid = new GridPane();
    Label src = new Label("Path Start Selector:");
    TextField startText = new TextField();
    src.setId("pathstartselector");
    startText.setId("starttext");

    // creates elements for end selector
    Label dst = new Label("Path End Selector: ");
    TextField endText = new TextField();
    dst.setId("pathendselector");
    endText.setId("endtext");
    
    // sets elements locations in GridPane and adds them
    GridPane.setConstraints(src, 0, 0);
    GridPane.setConstraints(startText, 1, 0);
    GridPane.setConstraints(dst, 0, 1);
    GridPane.setConstraints(endText, 1, 1);
    grid.getChildren().addAll(src, dst, startText, endText);
    // sets the width of column 0 in GridPane to align text boxes
    grid.getColumnConstraints().add(new ColumnConstraints(120));
    // sets alignment of GridPane and adds it to scene
    grid.setLayoutX(32);
    grid.setLayoutY(16);
    parent.getChildren().add(grid);
    
    // add functionality (LATER)
    Button find = new Button("Submit/Find Button");
    find.setId("findbutton");
    find.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        // TODO CALL METHODS!
        // stores the start, via, and end locations
        String start = startText.getText();
        String via = ((TextField)((HBox) parent.getChildren().get(4)).getChildren().get(1)).getText();
        String end = endText.getText();
        // updates the label of path
        ((Label) parent.getChildren().get(2)).setText(getPathLabel(start,via,end));
        }
  });
    // adjusts the layout of the button and adds it to the scene
    find.setLayoutX(32);
    find.setLayoutY(80);
    parent.getChildren().add(find);
  }
  
  /**
   * Creates the controls for displaying the shortest path returned by the search.
   * @param the parent pane that contains all controls
   */
  @Override
  public void createPathListDisplay(Pane parent) {
    // creates and configures a label to display the paths
    Label path = new Label();   
    path.setId("path");
    path.setLayoutX(32);
    path.setLayoutY(112);
    parent.getChildren().add(path);
  }
  
  /**
   * Creates controls for the two features in addition to the shortest path search.
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createAdditionalFeatureControls(Pane parent) {
    // calls the method to create the additional features
    this.createTravelTimesBox(parent);
    this.createOptionalLocationControls(parent);
  }
  
  /**
   * Creates the check box to add travel times in the result display.
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createTravelTimesBox(Pane parent) {
    // TODO CHANGE
    // creates a checkbox for controlling the showTimes varaible
    CheckBox travelTimesBox = new CheckBox("Show Walking Times");
    travelTimesBox.setId("ttbox");
    travelTimesBox.setLayoutX(200);
    travelTimesBox.setLayoutY(80);
    
    // creates an event handler for the checkbox to determine if times should be displayed
    travelTimesBox.selectedProperty().addListener(
        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
          if (travelTimesBox.isSelected() == true)
            showTimes = true;
          else 
            showTimes = false;
        });
    
    parent.getChildren().add(travelTimesBox);
  }
  
  /**
   * Creates controls to allow users to add a third location for the path to go through.
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createOptionalLocationControls(Pane parent) {
    // TODO CHANGE  
    Label locationSelector = new Label("Via Location (optional):");
    TextField locationBox = new TextField();
    locationBox.setId("viatext");
    locationSelector.setId("locationselector");
    // creates an HBox to store the textfield and label
    HBox hbox = new HBox();
    hbox.setLayoutX(500);
    hbox.setLayoutY(16);
    hbox.getChildren().add(locationSelector);
    hbox.getChildren().add(locationBox);
    hbox.setSpacing(8);
    parent.getChildren().add(hbox);
    // creates the checkbox
    CheckBox useVia = new CheckBox("Use Above Location in Path");
    useVia.setId("useviabox");
    useVia.setLayoutX(500);
    useVia.setLayoutY(48);
    
    // creates an event handler for the checkbox to determine if the via location should be used
    useVia.selectedProperty().addListener(
        (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
          if (useVia.isSelected() == true)
            useViaLocation = true;
          else 
            useViaLocation = false;
        });
    parent.getChildren().add(useVia);
  }
  
  /**
   * Creates an about and quit button.
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createAboutAndQuitControls(Pane parent) {
    // CHANGE
    // configures the about button and a label to represent it's output
    Button about = new Button("About");
    about.setId("about");
    about.setLayoutX(32);
    about.setLayoutY(560);
    parent.getChildren().add(about);

    Label aboutLabel = new Label();   
    aboutLabel.setId("aboutlabel");
    aboutLabel.setLayoutX(32);
    aboutLabel.setLayoutY(410);
    parent.getChildren().add(aboutLabel);
    
    // event handler to show or hide the about text when the button is pressed
    about.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        // flip if the text should be shown or not
          showAbout = !showAbout;
          // if showAbout is true, show the label
          if (showAbout) 
            aboutLabel.setText("Controls:\n\t* Start Location is used to specify where you would like"
                + "to start from\n\t* End Location is used to specify your destination\n\t" 
                + "* Show Walking Times allows you to see the walking time between locations and the total walking time\n\t"
                + "* Via Location allows you to specify a location to visit in between your start and end locations\n\t"
                + "* Use Above Location in Path will allow you to toggle where to include the via location in your path\n\t"
                + "* Submit/Find Path will use your inputs to calculate the best path for you to take");
          else 
            aboutLabel.setText("");      
        }
  }); 
    
    
    // configures the quit button
    Button quit = new Button("Quit");
    quit.setId("quit");
    quit.setLayoutX(96);
    quit.setLayoutY(560);
    
    // makes the quit button successfully close the application window
    quit.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
          javafx.application.Platform.exit();
        }
  });    
    parent.getChildren().add(quit);
  }
}
