runApp: App.java
	javac --module-path ../javafx/lib --add-modules javafx.controls App.java
	javac --module-path ../javafx/lib --add-modules javafx.controls FrontendImplementation.java
	javac Backend.java
	javac BaseGraph.java
	javac DijkstraGraph.java
	javac HashtableMap.java
	java --module-path ../javafx/lib --add-modules javafx.controls App
runTests: FrontendDeveloperTests.class
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests
FrontendDeveloperTests.class: FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
runBackendTests: BackendDeveloperTests.class
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests
BackendDeveloperTests.class: BackendDeveloperTests.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
clean:
	rm *.class
