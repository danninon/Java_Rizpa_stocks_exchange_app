package users.singleUserTab;

import DTO.InstructionDTO;
import DTO.UserDTO;
import SystemEngine.StocksTradeSystem;
import SystemEngine.User;
import appControl.ApplicationControl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SingleUserTabController {
    @FXML
    private AnchorPane anchorTradeControl;
    @FXML
    private ComboBox<String> comboBoxChooseOperator;
    @FXML
    private ComboBox<String> comboBoxChooseStock;
    @FXML
    private ToggleGroup tgTradeDirection;
    @FXML
    private ToggleGroup tgInstructionType;
    @FXML
    private TextField textFieldUserOffer;
    @FXML
    private TextField textFieldUserQuantityText;
    @FXML
    private Button buttonCommitInstruction;
    @FXML
    private RadioButton RBBuyer;
    @FXML
    private RadioButton RBSeller;
    @FXML
    private RadioButton RBLMT;
    @FXML
    private RadioButton RBMKT;

    @FXML
    private TabPane tabPaneUsers;
    @FXML
    private Tab singleUserTab;


    //    @FXML private Tab tabSell;
//    @FXML private TableView<InstructionDTO> tableViewSaleBook;
//    @FXML private TableColumn<InstructionDTO, Integer> tabSellPrice;
//    @FXML private TableColumn<InstructionDTO, Integer> tabSellQuantity;
//    @FXML private TableColumn<InstructionDTO, String> tabSellDate;
//    @FXML private TableColumn<InstructionDTO, String> tabSellOriginal;
//    @FXML private TableColumn<InstructionDTO, String> tabSellTrader;

    @FXML
    private TableView<UserDTO.StockPaperDTO> userTableView;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, String> colUserCompany;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, String> colUserSymbol;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, Integer> colUserPrice;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, Integer> colUserQuantity;

    @FXML
    private Label labelChosenStock;
    @FXML
    private Label labelUserValues;
    @FXML
    private Label labelUserName;
    private final StringProperty userNameProperty;
    private final StringProperty userValueProperty;
    private final StringProperty symbolChosenProperty;
    private UserDTO user;
    private StocksTradeSystem marketManager;
    private ApplicationControl appControl;
    public SingleUserTabController() {
        userNameProperty = new SimpleStringProperty("");
        userValueProperty = new SimpleStringProperty("0");
        symbolChosenProperty = new SimpleStringProperty("Enter Symbol here");
    }

    public void initialize() {
        labelUserName.textProperty().bind(userNameProperty);
        labelUserValues.textProperty().bind(userValueProperty);
        labelChosenStock.textProperty().bind(symbolChosenProperty);
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @FXML
    private void commitTradeListener() throws Exception {

        try {
            checkSystematicLegalInput();
            InstructionDTO gatheredInstruction = gatherInstructionDTO();
            appControl.TradeCommit(gatheredInstruction, comboBoxChooseStock.getSelectionModel().getSelectedItem(), RBBuyer.isSelected()); // (instruction,symbol)
        } catch (Exception e) {
            appControl.throwMainApplication(e);
        }



    }

    private void checkSystematicLegalInput() throws Exception {

        String chosenStock = comboBoxChooseStock.getSelectionModel().getSelectedItem();
        //symbol
        if (chosenStock.equals("Enter symbol here"))
            throw new Exception("Illegal operation:\nYou haven't chosen a stock to operate on.");


        //price
        int enteredPrice = Integer.parseInt(textFieldUserOffer.getText());
        if (enteredPrice <= 0) {
            throw new Exception("Illegal Price Error: the entered price must be a positive number, you nuts?");
        }
        //quantity

        int enteredQuantity = Integer.parseInt(textFieldUserQuantityText.getText());

//        if (enteredQuantity > 0) {
        UserDTO.StockPaperDTO searchedPaper = null;
        List<UserDTO.StockPaperDTO> userStocksBook = appControl.getUserStocksBook(labelUserName.getText());

        for (UserDTO.StockPaperDTO paper : user.getOwnedStocks()) { //finds right paper to search for possetions
            if (paper.getSymbol().equals(chosenStock)) {
                searchedPaper = paper;
                break;
            }
        }
        if (searchedPaper == null) {
            throw new Exception("Stock not found Error: " + userNameProperty.getValue() + " has no stocks of type - " +
                    comboBoxChooseStock.getSelectionModel().getSelectedItem() + " at his possetion. Lmao.");
        } else {
            if (!RBBuyer.isSelected()) {
                if (enteredQuantity > searchedPaper.getQuantity()) {
                    throw new Exception("Insufficient Quantity Error: " + userNameProperty.getValue() + " has only " + searchedPaper.getQuantity() + " stocks" +
                            " Whilst has tried to sell " + enteredQuantity + ". Lol."); //Insuffisient (not empty)
                }
            }
        }
    }

        //throws exception in case of illegal input
        private InstructionDTO gatherInstructionDTO () {
            LocalDateTime timeMark = LocalDateTime.now();
            InstructionDTO createdInstruction = null;

            createdInstruction = new InstructionDTO();
            createdInstruction.setTime(timeMark);
            //DO: if null\not found... paint red
            createdInstruction.setBuy(RBBuyer.isSelected());
            createdInstruction.setInstructionType(RBLMT.isSelected() ? RBLMT.getText() : RBMKT.getText());

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
        @FXML private void symbolTradeListener (ActionEvent event){


        }

        List<User> userList;

        public void setMainController (ApplicationControl applicationController){
            this.appControl = applicationController;
        }


        public void wireNewUser (User user, String key){
        }


        //tabTransactionPrice.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("price"));
        //        tabTransactionQuantity.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("quantity"));
//        tabTransactionDate.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("strTime"));
//        tabTransactionOriginal.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("instructionType"));
//        tabTransactionSeller.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("seller"));
//        tabTransactionBuyer.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("buyer"));
//        tableViewTransactionBook.getItems().setAll(FXCollections.observableArrayList(stock.getTransactionList()));
        private void loadUserStocksTable (StocksTradeSystem system, String userName){

            colUserSymbol.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("symbol"));
            colUserQuantity.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("quantity"));
            colUserCompany.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("companyName"));
            colUserPrice.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("currentPrice"));
            userTableView.getItems().setAll(FXCollections.observableArrayList(system.getSafeUser(userName).getOwnedStocks()));
        }


        public void wiringXMLtoTab (StocksTradeSystem system, String userName) throws Exception {


            setForm(system);
            userNameProperty.setValue(userName);
            userValueProperty.setValue(Integer.toString(system.getUserTotalVal(userName)));
            loadUserStocksTable(system, userName);


        }

        private void setForm (StocksTradeSystem system){
            comboBoxChooseStock.getItems().addAll(system.getSafeStocks().keySet());
        }

        public void wireNewUser (String key,int userTotalVal){

        }
    }
