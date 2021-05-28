package outputUsers.singleUserTab;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.StockDTO;
import SystemEngine.User;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import appControl.SubController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleUserTabController  {
    @FXML private AnchorPane anchorTradeControl;
    @FXML private ComboBox<String> comboBoxChooseOperator;
    @FXML private ComboBox<String> comboBoxChooseStock;
    @FXML private ToggleGroup tgTradeDirection;
    @FXML private ToggleGroup tgInstructionType;
    @FXML private TextField textFieldUserOffer;
    @FXML private TextField textFieldUserQuantityText;
    @FXML private Button buttonCommitInstruction;
    @FXML private RadioButton RBBuyer;
    @FXML private RadioButton RBSeller;
    @FXML private RadioButton RBLMT;
    @FXML private RadioButton RBMKT;

    @FXML private TabPane tabPaneUsers;
    @FXML private Tab singleUserTab;
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> colUserCompany;
    @FXML private TableColumn<User, String> colUserSymbol;
    @FXML private TableColumn<User, Integer> colUserPrice;
    @FXML private TableColumn<User, Integer> colUserQuantity;

    @FXML private Label labelUserValues;
    @FXML private Label labelUserName;

    void initialize(){
        labelUserName.textProperty().bind(userNameProperty);
        labelUserValues.textProperty().bind(userValueProperty);
    }
    private StringProperty userNameProperty;
    private StringProperty userValueProperty;

    private StocksTradeSystem marketManager;
    private ApplicationControl appControl;


    public SingleUserTabController() {
     userNameProperty = new SimpleStringProperty("");
     userValueProperty = new SimpleStringProperty("0");
    }


    @FXML private void commitTradeListener(){
        try {
        String symbol = comboBoxChooseStock.getSelectionModel().getSelectedItem();
        appControl.TradeCommit(gatherInstructionDTO(), comboBoxChooseStock.getSelectionModel().getSelectedItem(), RBBuyer.isSelected()); // (instruction,symbol)


        } catch (Exception e) {
            appControl.ExhibitErrorMain(e.getMessage());
        }
}
    InstructionDTO gatherInstructionDTO() throws Exception {
        InstructionDTO createdInstruction = new InstructionDTO();
        //DO: if null\not found... paint red
        createdInstruction.setBuy(RBBuyer.isSelected());
        createdInstruction.setInstructionType(RBLMT.isSelected() ? RBLMT.getText() : RBMKT.getText());
        LocalDateTime timeMark = LocalDateTime.now();
        createdInstruction.setTime(timeMark);
        createdInstruction.setStrTime(timeMark.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")));
        System.out.println((textFieldUserOffer.getText()));
        System.out.println((textFieldUserQuantityText.getText()));
        createdInstruction.setPrice(Integer.parseInt(textFieldUserOffer.getText()));
        createdInstruction.setQuantity(Integer.parseInt(textFieldUserQuantityText.getText()));
        createdInstruction.setOperatorName(userNameProperty.getValue());
        //createdInstruction.setOperatorName(comboBoxChooseOperator.getSelectionModel().getSelectedItem());
        return createdInstruction;
    }

    @FXML private ComboBox<String> CBSymbolTrade;
    @FXML private void symbolTradeListener(ActionEvent event) {

    }



    public void setMainController(ApplicationControl applicationController) {
        this.appControl = applicationController;
    }


    public void wireNewUser(User user, String key) {}





        private void updateUsers() {
//        colUserPrice.setCellValueFactory(new PropertyValueFactory<User, Integer>( ));
//        colUserQuantity.setCellValueFactory(new PropertyValueFactory<User, Integer>("quantity"));
//        colUserCompany.setCellValueFactory(new PropertyValueFactory<User, String>("strTime"));
//        colUserSymbol.setCellValueFactory(new PropertyValueFactory<User, String>("instructionType"));

            userTableView.getItems().setAll((User) FXCollections.observableMap(marketManager.getUsers()));



    }


    public void wiringXMLtoTab(StocksTradeSystem system, String key) {
        setForm(system);
        try {
            userNameProperty.setValue(key);
            userValueProperty.setValue(String.valueOf(marketManager.getUserTotalVal(key)));
        } catch (Exception e) {
            appControl.ExhibitErrorMain(e.getMessage());
        }

    }

    private void setForm(StocksTradeSystem system) {
        comboBoxChooseStock.getItems().addAll(system.getSafeStocks().keySet());
    }

    public void wireNewUser(String key, int userTotalVal) {

    }
}
