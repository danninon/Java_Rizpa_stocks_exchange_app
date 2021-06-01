package Components.outputScreen.outputStocks;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.StockDTO;
import DTO.TransactionDTO;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import appControl.SubController;
import appControl.SubScreen;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OutputScreenController implements SubScreen {

//    @FXML private TabPane actionsBar;
//    @FXML private Tab mainTabIntroduction;
//    @FXML private Tab mainTabOpenStocksBook;
    @FXML private Tab getMainTabTrade;

    @FXML private Tab StocksBook;
    @FXML private Tab mainTabTrade;
    @FXML private Tab TabTrade;

    @FXML private Tab TabTransaction;
    @FXML private TableView<TransactionDTO> tableViewTransactionBook;
    @FXML private TableColumn<TransactionDTO, Integer> tabTransactionPrice;
    @FXML private TableColumn<TransactionDTO, Integer> tabTransactionQuantity;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionDate;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionOriginal;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionBuyer;
    @FXML private TableColumn<TransactionDTO, String> tabTransactionSeller;

    @FXML private Tab tabSell;
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

    @FXML private Label bookTableHeader;

    @FXML private TableView<TransactionDTO> tableViewTrade;
    @FXML private TableColumn<TransactionDTO, String> tabTradeTabDate;
    @FXML private TableColumn<TransactionDTO, String> tabTradeTabType;
    @FXML private TableColumn<TransactionDTO, Integer> tabTradeTabQuantity;
    @FXML private TableColumn<TransactionDTO, Integer> tabTradeTabPrice;

    @FXML private TextArea textAreaInfo;

    StringProperty searchedStock;

    @Override public void setManager(StocksTradeSystem marketManager) { this.marketManager = marketManager;    }
    @Override public void setMainController(ApplicationControl mainController) {
        this.mainController = mainController;
    }


    public void updateStocksBookSearchedSymbol(StockDTO stock, String enteredSymbolText) {
        updateBuyBook(stock);
        updateSaleBook(stock);
        updateSaleTransactionsBook(stock);
        searchedStock.set("Company name: " + stock.getCompanyName() + " & " + "Stocks symbol: " + enteredSymbolText);

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

    public void updateTradeScreen(CMD4ReturnBundle bundle) { // opens the bundle to the screen in the stages
        //1 - update Table
        if (bundle.getTransactionsMade() != null) {

            List<TransactionDTO> listInBundle = bundle.getTransactionsMade();
            tabTradeTabDate.setCellValueFactory(new PropertyValueFactory<>("strTime"));
            tabTradeTabType.setCellValueFactory(new PropertyValueFactory<>("instructionType"));
            tabTradeTabPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            tabTradeTabQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            ObservableList<TransactionDTO> list = FXCollections.observableArrayList(bundle.getTransactionsMade());
            tableViewTrade.getItems().setAll(list);
        }
//2 - update Info box
        textAreaInfo.clear();
        boolean isBuy = mainController.isCurrentlyBuying();
        if (bundle.getInsDTO() == null) {

            if (isBuy) {
                textAreaInfo.setText("Perfect match found!\nSuccessfully acquired the full extent of the request\n. ");
            } else {
                textAreaInfo.setText("Perfect match found!\nSuccessfully sold the full extent of the request\n. ");
            }

        } else if (bundle.getTransactionsMade().size() != 0) {

            if (isBuy) {
                textAreaInfo.appendText("Partially match found!\nSuccessfully bought some of the request.\n");
            } else {
                textAreaInfo.appendText("Partially match found!\nSuccessfully sold some of the request.\n");
            }

            if (isBuy) {
                textAreaInfo.appendText("\nThis partial updateUserAfterBuying instruction was been added to the market(the reminder after partially buying some of the stocks): \n");
            } else {
                textAreaInfo.appendText("\nThis partial sale instruction was added to the market(the reminder after partially selling some of the stock): \n");
            }
            textAreaInfo.appendText(instructionTimePriceQuantity(bundle.getInsDTO()));
        } else {
            if (isBuy)
                textAreaInfo.appendText("There are no active sale instruction that matches with your request.\n");
            else
                textAreaInfo.appendText("There are no active updateUserAfterBuying instruction that matches with your request.\n");
            if (isBuy)
                textAreaInfo.appendText("The full updateUserAfterBuying instruction that has been added to the market. \n");
            else
                textAreaInfo.appendText("The full sale instruction that has been added to the market. \n");
        }
    }






//    public ObservableList<InstructionDTO> getProduct(StockDTO stock) {
//
//        return FXCollections.observableArrayList(stock.getBuyList());
//    }

//    public void LoadMiddle(StockDTO stock, String symbol) {
//        updateStocksBookSearchedSymbol(stock);
//        bookTableHeader.setText("Symbol: " + symbol + " | " + "Company name: " + stock.getCompanyName());
//    }

    public String stockHeaderFormat(int i) {
        return "|~~~~~~~~~~~~~~~~~~~~" + (i) + "~~~~~~~~~~~~~~~~~~~~|\n";
    }

    public String instructionTimePriceQuantity(InstructionDTO ins) {
        return "Time - " + ins.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\nPrice - " + ins.getPrice() +
                "\nQuantity - " + ins.getQuantity();

    }

    public OutputScreenController(){
        searchedStock = new SimpleStringProperty("Searched Stock");
        System.out.println("body control constructor");
    }

    public void initialize() {
        System.out.println("Output screen controller init");
        bookTableHeader.textProperty().bind(searchedStock);
        tableViewBuyBook.editableProperty().setValue(false);
        textAreaInfo.editableProperty().setValue(false);


//        actionsBar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                    if((newValue.getText().equals(mainTabOpenStocksBook.getText()))) {
//                        System.out.println("in main StocksBook tab");
//                        try {
//                            mainController.requestLoadUI(SubController.Type.STOCKS_BOOK);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else if((newValue.getText().equals("Trade"))){ //change the connected user only if select tab that not Admin
//                        System.out.println("in main trade tab");
//                        try {
//                            mainController.requestLoadUI(SubController.Type.TRADE);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else if((newValue.getText().equals(mainTabIntroduction.getText()))){ //change the connected user only if select tab that not Admin
//                        System.out.println("in main introduction tab");
//                        try {
//                            mainController.requestLoadUI(SubController.Type.INTRODUCTION);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });


    }
    private SubController thisController;
    private ApplicationControl mainController;
    private StocksTradeSystem marketManager;

   public String presentingTransaction(TransactionDTO tr) {
        return "Time - " + tr.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\nPrice - " + tr.getPrice() +
                "\nQuantity - " + tr.getQuantity() + "\nCycle - " + tr.getTotalPayment();
    }


}

//    public TableView<InstructionDTO> updateStocksBookSearchedSymbol(StockDTO stock) {
//        tabBuyPrice = new TableColumn<>("Price");
//        tabBuyPrice.setMinWidth(200);
//        tabBuyPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//
//        tabBuyQuantity = new TableColumn<>("Quantity");
//        tabBuyQuantity.setMinWidth(200);
//        tabBuyQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//
//
//        tabBuyDate = new TableColumn<>("Date\\Time");
//        tabBuyDate.setMinWidth(200);
//        tabBuyDate.setCellValueFactory(new PropertyValueFactory<>("time"));
//
//        tabBuyOriginal = new TableColumn<>("Original Type");
//        tabBuyOriginal.setMinWidth(200);
//        tabBuyOriginal.setCellValueFactory(new PropertyValueFactory<>("instructionType"));
//
//        tabBuyTrader = new TableColumn<>("Trader");
//        tabBuyTrader.setMinWidth(200);
//        tabBuyTrader.setCellValueFactory(new PropertyValueFactory<>("operatorName"));
//
//
//        purchaseBook = new TableView<>();
//        ObservableList<InstructionDTO> lst = getProduct(stock);
//        System.out.println(instructionTimePriceQuantity(lst.get(0)));
//        purchaseBook.setItems(lst);
//
//       purchaseBook.getColumns().add(tabBuyPrice);
//        purchaseBook.getColumns().add(tabBuyQuantity);
//        purchaseBook.getColumns().add(tabBuyDate);
//        purchaseBook.getColumns().add(tabBuyOriginal);
//        purchaseBook.getColumns().add(tabBuyTrader);
//
//        boolean status = purchaseBook.getColumns().addAll(tabBuyPrice, tabBuyQuantity, tabBuyDate, tabBuyOriginal,tabBuyTrader);
//        System.out.println(status);
//
//
//
////        int index = 1;
////        System.out.println("Active investors are looking to purchase - ");
////        if (stock.getBuyList().isEmpty()) {
////            System.out.println("No one is looking for stocks!");
////        } else {
////            for (InstructionDTO ins : stock.getBuyList()) {
////                System.out.println(stockHeaderFormat(index++) + instructionTimePriceQuantity(ins));
////            }
////        }
////        System.out.println(lst.get(0).getPrice() + " " + lst.get(0).getQuantity());
//
//        return purchaseBook;
//    }

//  if(actionsBar.getSelectionModel().getSelectedIndex() == 0){
//        System.out.println("0");
//    }
//        if(actionsBar.getSelectionModel().getSelectedIndex() == 1){
//        System.out.println("1");
//    }
//        if(actionsBar.getSelectionModel().getSelectedIndex() == 2){
//        System.out.println("2");

//            if((newValue.getText().equals(mainTabIntroduction.getText()))){ //change the connected user only if select tab that not Admin
//                System.out.println("in main introduction tab");
//                try {
//                    mainController.requestLoadUI(SubController.Type.INTRODUCTION);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if((newValue.getText().equals("Trade"))){ //change the connected user only if select tab that not Admin
//                System.out.println("in main trade tab");
//                try {
//                    mainController.requestLoadUI(SubController.Type.TRADE);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if((newValue.getText().equals(mainTabOpenStocksBook.getText()))) {
//                System.out.println("in main StocksBook tab");
//                try {
//                    mainController.requestLoadUI(SubController.Type.STOCKS_BOOK);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


//    @FXML
//    void tradeListener(ActionEvent event) {
//        System.out.println("here");
//     //   mainController.requestLoadTradeUI();
//    }



