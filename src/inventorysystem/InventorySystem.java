/*
 * Program: InventorySystem.java
 * Author: Davis Nguyen
 * Date: 8/19/2019 created, added css stylesheet
 *
 */
package inventorysystem;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventorySystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //load FXMLDocument.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/FXMLInventorySystem.fxml"));

        //create scene and set stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/resources/css/InventorySystem.css");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launches stage
        launch(args);
    }

}
