package usersTabPane.adminTab;

import DTO.InstructionDTO;
import DTO.StockDTO;
import DTO.TransactionDTO;
import DTO.UserDTO;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminTabController {

    public AdminTabController(){

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
    @FXML private TableColumn<InstructionDTO, String> tabSellTrader;


    @FXML private TableView<InstructionDTO> tableViewBuyBook;
    @FXML private TableColumn<InstructionDTO, Integer> tabBuyPrice;
    @FXML private TableColumn<InstructionDTO, Integer> tabBuyQuantity;
    @FXML private TableColumn<InstructionDTO, String> tabBuyDate;
    @FXML private TableColumn<InstructionDTO, String> tabBuyOriginal;
    @FXML private TableColumn<InstructionDTO, String> tabBuyTrader;


// private void loadStockTables(StocksTradeSystem system, String userName) {
//
//        colUserSymbol.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("symbol"));
//        colUserCompany.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("companyName"));
//        colUserPrice.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("currentPrice"));
//        colIdle.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("idleQuantity"));
//        colAtMarket.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("atMarketQuantity"));
//        colTotal.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("totalQuantity"));
//
//
//
//        userTableView.getItems().setAll(FXCollections.observableArrayList(system.getSafeUser(userName).getOwnedStocks()));
//    }
    @FXML
    void adminSearchStockListener(ActionEvent event) throws Exception {
try {
    String stockSymbol = comboBoxChooseStock2.getSelectionModel().getSelectedItem();
    if (applicationControl.systemBootFinish()) {
        if (stockSymbol != null)
            loadStockTables(applicationControl.getMarketManager(), stockSymbol);
        else {
            throw new Exception("Error: You'll have to choose a stock in the admin tab in order to view it's market.");
        }
    }
    else {
        throw new Exception("System unloaded Error: Please load the system first.");
    }
}
        catch (Exception e){
            applicationControl.throwMainApplication(e);
        }
    }

    private void updateSaleTransactionsBook(StockDTO stock) {
        tabTransactionPrice.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("price"));
        tabTransactionQuantity.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("quantity"));
        tabTransactionDate.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("strTime"));
        tabTransactionOriginal.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("instructionType"));
        tabTransactionSeller.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("seller"));
        tabTransactionBuyer.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("buyer"));
        tableViewTransactionBook.getItems().setAll(FXCollections.observableArrayList(stock.getTransactionList()));

    }

    private void updateSaleBook(StockDTO stock) {
        tabSellPrice.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("price"));
        tabSellQuantity.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("quantity"));
        tabSellDate.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("strTime"));
        tabSellOriginal.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("instructionType"));
        tabSellTrader.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("operatorName"));
        tableViewSaleBook.getItems().setAll(FXCollections.observableArrayList(stock.getSaleList()));
    }

    private void updateBuyBook(StockDTO stock) {
        tabBuyPrice.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("price"));
        tabBuyQuantity.setCellValueFactory(new PropertyValueFactory<InstructionDTO, Integer>("quantity"));
        tabBuyDate.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("strTime"));
        tabBuyOriginal.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("instructionType"));
        tabBuyTrader.setCellValueFactory(new PropertyValueFactory<InstructionDTO, String>("operatorName"));
        tableViewBuyBook.getItems().setAll(FXCollections.observableArrayList(stock.getBuyList()));
    }

    private void loadStockTables(StocksTradeSystem system, String symbol) throws Exception {
        StockDTO loadedStock = system.getSafeStock(symbol);
        updateBuyBook(loadedStock);
        updateSaleBook(loadedStock);
        updateSaleTransactionsBook(loadedStock);
    }

    public void wiringXMLtoTab(StocksTradeSystem system) throws Exception {
        setForm(system);
        if (applicationControl.systemBootFinish()) {
            if (comboBoxChooseStock2.getSelectionModel().getSelectedItem() != null) {
                loadStockTables(system, comboBoxChooseStock2.getSelectionModel().getSelectedItem());
            }
        }
    }

    private void setForm(StocksTradeSystem system) {
        comboBoxChooseStock2.getItems().addAll(system.getSafeStocks().keySet());
    }

    public void setMainController(ApplicationControl applicationControl){this.applicationControl = applicationControl;}
    ApplicationControl applicationControl;
}
