package usersTabPane.adminTab;

import DTO.InstructionDTO;
import DTO.StockDTO;
import DTO.TransactionDTO;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminTabController {

    public AdminTabController(){
        previousStockSymbol = new SimpleStringProperty();
    }

    public void initialize(){
        comboBoxChooseStock2.promptTextProperty().bind(previousStockSymbol);
    }

    @FXML private ComboBox<String> comboBoxChooseStock2;
    @FXML private TableView<TransactionDTO> tableViewTransactionBook;
    @FXML private TableColumn<TransactionDTO, Integer> tabTransactionPrice;
    @FXML private TableColumn<TransactionDTO, Integer> tabTransactionQuantity;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionDate;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionOriginal;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionBuyer;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionSeller;

    @FXML private TableView<InstructionDTO> tableViewSaleBook;
    @FXML private TableColumn<InstructionDTO, Integer> tabSellPrice;
    @FXML private TableColumn<InstructionDTO, Integer> tabSellQuantity;
    @FXML private TableColumn<InstructionDTO, String> tabSellDate;
    @FXML private TableColumn<InstructionDTO, String> tabSellOriginal;
    @FXML
    private TableColumn<InstructionDTO, String> tabSellTrader;


    @FXML
    private TableView<InstructionDTO> tableViewBuyBook;
    @FXML
    private TableColumn<InstructionDTO, Integer> tabBuyPrice;
    @FXML
    private TableColumn<InstructionDTO, Integer> tabBuyQuantity;
    @FXML
    private TableColumn<InstructionDTO, String> tabBuyDate;
    @FXML
    private TableColumn<InstructionDTO, String> tabBuyOriginal;
    @FXML
    private TableColumn<InstructionDTO, String> tabBuyTrader;

    @FXML
    private TableView<ApplicationControl.UserAction> tableActionsHistory;
    @FXML
    private TableColumn<ApplicationControl.UserAction, Integer> colHistoryActionNumber;
    @FXML
    private TableColumn<ApplicationControl.UserAction, String> colHistoryMessage;
    @FXML
    private TableColumn<ApplicationControl.UserAction, String> colHistoryTime;


    @FXML
//    void adminSearchStockListener(ActionEvent event) throws Exception {
//try {
//    String stockSymbol = comboBoxChooseStock2.getSelectionModel().getSelectedItem();
//    if (applicationControl.systemBootFinish()) {
//        if (stockSymbol != null)
//            loadAdminStocks(applicationControl.getMarketManager(), stockSymbol, event);
//        else {
//            throw new Exception("Error: You'll have to choose a stock in the admin tab in order to view it's market.");
//        }
//    }
//    else {
//        throw new Exception("System unloaded Error: Please load the system first.");
//    }
//}
//        catch (Exception e){
//            applicationControl.throwMainApplication(e);
//        }
//    }
    private SimpleStringProperty previousStockSymbol;
    private void loadTransactionsBook(StockDTO stock) {
        tabTransactionPrice.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("price"));
        tabTransactionQuantity.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("quantity"));
        tabTransactionDate.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("strTime"));
        tabTransactionOriginal.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("instructionType"));
        tabTransactionSeller.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("sellerName"));
        tabTransactionBuyer.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("buyerName"));
        tableViewTransactionBook.getItems().setAll(FXCollections.observableArrayList(stock.getTransactionList()));

    }

    private void loadSaleBook(StockDTO stock) {
        tabSellPrice.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("price"));
        tabSellQuantity.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("quantity"));
        tabSellDate.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("strTime"));
        tabSellOriginal.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("instructionType"));
        tabSellTrader.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("operatorName"));
        tableViewSaleBook.getItems().setAll(FXCollections.observableArrayList(stock.getSaleList()));
    }

    private void loadBuyBook(StockDTO stock) {
        tabBuyPrice.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("price"));
        tabBuyQuantity.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("quantity"));
        tabBuyDate.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("strTime"));
        tabBuyOriginal.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("instructionType"));
        tabBuyTrader.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("operatorName"));
        tableViewBuyBook.getItems().setAll(FXCollections.observableArrayList(stock.getBuyList()));
    }


    private void loadHistoryBook(List<ApplicationControl.UserAction> historyLog) {
        colHistoryActionNumber.setCellValueFactory(new PropertyValueFactory<ApplicationControl.UserAction, Integer>("actionNum"));
        colHistoryMessage.setCellValueFactory(new PropertyValueFactory<ApplicationControl.UserAction, String>("details"));
        colHistoryTime.setCellValueFactory(new PropertyValueFactory<ApplicationControl.UserAction, String>("strTime"));
        tableActionsHistory.getItems().setAll(FXCollections.observableArrayList(historyLog));
    }


    private void setForm(StocksTradeSystem system) {
        comboBoxChooseStock2.getItems().addAll(system.getSafeStocks().keySet());
    }

    public void setMainController(ApplicationControl applicationControl) {
        this.applicationControl = applicationControl;
    }

    ApplicationControl applicationControl;

    public String getOpenStock() {
        String retOpenStockSymbol = null;
        SelectionModel<String> selectedStockModel = comboBoxChooseStock2.getSelectionModel();
        if (selectedStockModel != null){
            retOpenStockSymbol = selectedStockModel.getSelectedItem();
        }

        return retOpenStockSymbol;
    }

//    public void setAdminSelectedStock(String openStock) {
//        comboBoxChooseStock2.setSelectionModel(openStock);
//    }

    public SingleSelectionModel<String> getSelectionModelStock() {
        return comboBoxChooseStock2.getSelectionModel();
    }

    public void setSymbolComboBox(String previousStockSymbol) {
        this.previousStockSymbol.set(previousStockSymbol);
    }

    public void loadAdminStocks(StocksTradeSystem system, String currentlySelectedStock, ActionEvent event) throws Exception {
        setForm(system);
        if (currentlySelectedStock != null) {
        StockDTO stock = system.getSafeStock(currentlySelectedStock);

            for (String symbol : comboBoxChooseStock2.getItems()) {
                if (symbol.equals(currentlySelectedStock)){
                    setSymbolComboBox(currentlySelectedStock);
                    loadTransactionsBook(stock);
                    loadSaleBook(stock);
                    loadBuyBook(stock);
                    break;
                }
            }
        }
    }


    public ComboBox<String> getSymbolCB() {
        return comboBoxChooseStock2;
    }

    public void updateAdminLog() {
        loadHistoryBook(applicationControl.getMyEventList());
    }
}
