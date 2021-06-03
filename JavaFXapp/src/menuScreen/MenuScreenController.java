package menuScreen;

import appControl.ApplicationControl;
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
    @FXML private Label labelCurrentCSS;

    StringProperty systemLoadedStatusString;
    StringProperty systemLoadedPathMSG;
    StringProperty currentCSSProperty;

    @FXML
    void ChangeCSSListener(ActionEvent event) throws Exception {
       mainController.changeCSS_Seq(event, currentCSSProperty);

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
             if(mainController.loadXMLFile(selectedFile.getPath(), event)) {
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
        currentCSSProperty = new SimpleStringProperty("Current CSS loaded: GoldenTiger");

    }

    public void setMainController(ApplicationControl mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        labelSystemStatus.textProperty().bind(systemLoadedStatusString);
        labelFilePathLoaded.textProperty().bind(systemLoadedPathMSG);
        labelCurrentCSS.textProperty().bind(currentCSSProperty);
        System.out.println("body init");
    }


    private ApplicationControl mainController;
    final static String XML_FILE_NAME1 = "C:\\ex2-small.xml"; //C:/Users/Z490/RSE/

    final String FALSE = "FALSE";
    final String TRUE = "TRUE";
}
