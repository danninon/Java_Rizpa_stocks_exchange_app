package appControl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ApplicationMain extends Application {


    public ApplicationMain() throws IOException {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource(MAIN_APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);

            BorderPane root = fxmlLoader.load(url.openStream());
            ApplicationControl appControl = fxmlLoader.getController();

            appControl.setPrimaryStage(primaryStage);
            Scene scene = new Scene(root, 1500, 1000);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch(args);
    }
    public final static String BODY_FXML_INCLUDE_RESOURCE = "/Components/outputScreen/outputStocks/outputScreen.fxml";
    public final static String ACTIONBAR_FXML_INCLUDE_RESOURCE = "/Components/inputScreen/stocksBookInputScreen/stocksBookInputScreen.fxml";
    public final static String MAIN_APP_FXML_INCLUDE_RESOURCE = "/appControl/marketAppContainer.fxml";

}
//    public void start2(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL url = getClass().getResource(ACTIONBAR_FXML_INCLUDE_RESOURCE);
//        fxmlLoader.setLocation(url);
//        VBox actionBar = fxmlLoader.load(url.openStream());
//        LeftComponentControl leftController = fxmlLoader.getController();
//
//        fxmlLoader = new FXMLLoader();
//        url = getClass().getResource(BODY_FXML_INCLUDE_RESOURCE);
//
//        fxmlLoader.setLocation(url);
//        TabPane body = fxmlLoader.load(url.openStream());
//        BodyControl BodyControl = fxmlLoader.getController();
//
//        // load body component and controller from fxml
//        fxmlLoader = new FXMLLoader();
//        url = getClass().getResource(MAIN_APP_FXML_INCLUDE_RESOURCE);
//        fxmlLoader.setLocation(url);
//        BorderPane root = fxmlLoader.load(url.openStream());
//        ApplicationControl appController = fxmlLoader.getController();
//
//        root.setLeft(actionBar);
//        root.setCenter(body);
//
//        appController.setLeftController(leftController);
//        appController.setBodyController(BodyControl);
//
//        Scene scene = new Scene(root, 600,400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
