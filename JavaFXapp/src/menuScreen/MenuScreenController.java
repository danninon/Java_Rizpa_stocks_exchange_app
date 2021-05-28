package menuScreen;

import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import appControl.SubController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class MenuScreenController {


    @FXML private Button buttonLoadXML;
    @FXML private Label labelSystemStatus;
    @FXML private Label labelFilePathLoaded;
    @FXML private HBox statusBox;

    StringProperty systemLoadedStatusString;
    StringProperty systemLoadedPathMSG;


    @FXML
    void virtualizationRequestListener(ActionEvent event) throws Exception {

    }

    private void informConnections() {

    }

    private void updateThis(String xmlFileName1) {
        systemLoadedPathMSG.set("Currenly loaded XML: " + XML_FILE_NAME1);
        systemLoadedStatusString.set("System's status: True");
    }

    @FXML
    public void xmlLoadingAttemptListener(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file to load it");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(mainController.getStage());
        if (selectedFile == null){
            return;
        }
        else {
            if (!labelSystemStatus.getText().equals("System's status: " + TRUE)) {
             if(mainController.loadXMLFile(selectedFile.getPath())) {
                 loadXMLLocal(selectedFile);
             }
            }
        }
    }

    private void loadXMLLocal(File selectedFile) {
        systemLoadedPathMSG.set("Currenly loaded XML: " + selectedFile.getPath());
        systemLoadedStatusString.set("System's status: True");
        statusBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public MenuScreenController(){
        systemLoadedStatusString = new SimpleStringProperty("System's Status: false");
        systemLoadedPathMSG = new SimpleStringProperty( "Currently XML loaded: Uninitialised");
    }

    public void setMainController(ApplicationControl mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        labelSystemStatus.textProperty().bind(systemLoadedStatusString);
        labelFilePathLoaded.textProperty().bind(systemLoadedPathMSG);
        System.out.println("body init");
    }


    private ApplicationControl mainController;
    final static String XML_FILE_NAME1 = "C:\\ex2-small.xml"; //C:/Users/Z490/RSE/

    final String FALSE = "FALSE";
    final String TRUE = "TRUE";
}
