import java.io.IOException;
import javafx.application.Application;

public class App {
  public static void main(String[] args) {
    System.out.println("v0.1");
    BackendInterface backend = new Backend(new DijkstraGraph<String,Double>());
    try {   
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {}
    FrontendImplementation.setBackend(backend);
    Application.launch(FrontendImplementation.class, args);
  }
}
