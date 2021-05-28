package Components.inputScreen.tradeInputScreen;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.StockDTO;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import appControl.SubController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TradeInputScreenController implements SubController {


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


    @FXML
    void userCommitsTradeInstructionListener(ActionEvent event) throws Exception {
        String symbol = comboBoxChooseStock.getSelectionModel().getSelectedItem();
        StockDTO fetchedStock = marketManager.getSafeStock(symbol);
        InstructionDTO newInstruction = gatherInstructionDTO();
        //if everything is okay, commit.
        CMD4ReturnBundle bundle =  marketManager.operateOnStocks(newInstruction, symbol);
//        appControl.requestUpdateTradeOutput(bundle);
//        appControl.requestUpdateStocksBook(symbol);
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
        createdInstruction.setOperatorName(comboBoxChooseOperator.getSelectionModel().getSelectedItem());
        return createdInstruction;
    }
//actionsBar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                    if((newValue.getText().equals(mainTabOpenStocksBook.getText()))) {
//                        System.out.println("in main StocksBook tab");
//                        try {
//                            mainController.requestLoadUI(SubController.Type.STOCKS_BOOK);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }

        //if (tgTradeDirection.getSelectedToggle().toString())
        //newInstruction.setOperatorName();
        //marketManager.operateOnStocks()

    // actionsBar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    //                    if((newValue.getText().equals(mainTabOpenStocksBook.getText()))) {

    @FXML
    void chooseOperatorListener(ActionEvent event) {

    }

    @FXML
    void priceEnteredListener(ActionEvent event) {

    }

    @Override
    public void setManager(StocksTradeSystem manager) {
        this.marketManager = manager;
    }

    @Override
    public void setMainController(ApplicationControl applicationController) {
        this.appControl = applicationController;
    }

    @Override
    public void submitData() {
        comboBoxChooseOperator.getItems().addAll(marketManager.getUsers().keySet());
        comboBoxChooseStock.getItems().addAll(marketManager.getSafeStocks().keySet());
    }



    //@Override

    public void initialize(){


       //comboBoxChooseOperator.getItems(FXCollections.observableSet(marketManager.getUsers().keySet()));
   }

    public ObservableList<InstructionDTO> getProduct(StockDTO stock) {

        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<InstructionDTO> products = FXCollections.observableArrayList(stock.getBuyList());


        return products;
    }

    StocksTradeSystem marketManager;
    ApplicationControl appControl;

    public boolean isCurrentlyBuying() {
        return RBBuyer.isSelected();
    }
}
//    public AnchorPane getController(){
//        return anchorTradeControl;
//    }
//    public TradeInputScreenController(AnchorPane control) {
//    }
//
//    public TradeInputScreenController() {
//        // leftController.getChildren().setAll(control);
//    }