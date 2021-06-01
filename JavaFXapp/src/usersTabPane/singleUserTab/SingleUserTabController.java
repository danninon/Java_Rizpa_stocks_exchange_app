package usersTabPane.singleUserTab;

import DTO.InstructionDTO;
import DTO.TransactionDTO;
import DTO.UserDTO;
import SystemEngine.StocksTradeSystem;
import SystemEngine.Transaction;
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
    private final StringProperty userNameProperty;
    private final StringProperty userValueProperty;



    private final StringProperty symbolChosenProperty;

    @FXML
    private TableView<TransactionDTO> tableOperationResult;

    @FXML
    private TableColumn<TransactionDTO, Integer> colSharedPrice;

    @FXML
    private TableColumn<TransactionDTO, Integer> colQuantity;

    @FXML
    private TableColumn<TransactionDTO, Integer> colTotal1;

    @FXML
    private TableColumn<TransactionDTO, String> colTime;

    @FXML
    private TableColumn<TransactionDTO, String> colOfferer;

    @FXML
    private Label labelPriceNewInstruction;

    @FXML
    private Label labelQuantityNewInstruction;

    @FXML
    private Label labelTypeNewInstruction1;

    @FXML
    private Label labelChosenStock;



    private final String ENTER_SYMBOL_HERE = "Enter Symbol here";
    List<User> userList;
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
    private TableColumn<UserDTO.StockPaperDTO, Integer> colAtMarket;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, Integer> colIdle;
    @FXML
    private TableColumn<UserDTO.StockPaperDTO, Integer> colTotal;
//    private final StringProperty priceFieldProperty;
//    private final StringProperty quantityFieldProperty;
    @FXML
    private Label labelUserValues;
    @FXML
    private Label labelUserName;
    @FXML
    private ComboBox<String> CBSymbolTrade;


    private UserDTO user;
    private StocksTradeSystem marketManager;
    private ApplicationControl appControl;

    public SingleUserTabController() {
        userNameProperty = new SimpleStringProperty("");
        userValueProperty = new SimpleStringProperty("0");
        symbolChosenProperty = new SimpleStringProperty(ENTER_SYMBOL_HERE);
//        priceFieldProperty = new SimpleStringProperty("1234");
//        quantityFieldProperty = new SimpleStringProperty("12");
    }

    public void initialize() {
//        textFieldUserOffer.textProperty().bind(priceFieldProperty);
//        textFieldUserOffer.setEditable(true);
//        textFieldUserQuantityText.textProperty().bind(quantityFieldProperty);
//        textFieldUserQuantityText.setEditable(true);

        textFieldUserOffer.setText("100");
        textFieldUserQuantityText.setText("10");
        comboBoxChooseStock.promptTextProperty().bind(symbolChosenProperty);
        labelUserName.textProperty().bind(userNameProperty);
        labelUserValues.textProperty().bind(userValueProperty);
//        labelChosenStock.textProperty().bind(symbolChosenProperty);
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @FXML
    private void commitTradeListener(ActionEvent event) throws Exception {

        try {
            checkSystematicLegalInput();
            InstructionDTO gatheredInstruction = gatherInstructionDTO();
            appControl.tradeCommit(gatheredInstruction, comboBoxChooseStock.getSelectionModel().getSelectedItem(), RBBuyer.isSelected(), event); // (instruction,symbol)

        } catch (Exception e) {
            appControl.throwMainApplication(e);
        }


    }

    private void checkSystematicLegalInput() throws Exception {

        String chosenStock = comboBoxChooseStock.getSelectionModel().getSelectedItem();
        //symbol
        if (chosenStock == null)
            throw new Exception("Illegal operation:\nYou haven't chosen a stock to operateStock on.");


        //price
        int enteredPrice = Integer.parseInt(textFieldUserOffer.getText());
        if (enteredPrice <= 0) {
            throw new Exception("Illegal Price Error: the entered price must be a positive number, you nuts?");
        }
        //quantity

        int enteredQuantity = Integer.parseInt(textFieldUserQuantityText.getText());

        if (enteredQuantity > 0) {

            List<UserDTO.StockPaperDTO> userStocksBook = appControl.getUserStocksBook(labelUserName.getText());
            UserDTO.StockPaperDTO searchedPaper = null;
            for (UserDTO.StockPaperDTO paper : userStocksBook) { //finds right paper to search for possetions
                if (paper.getSymbol().equals(chosenStock)) {
                    searchedPaper = paper;
                    break;
                }
            }
            if (!RBBuyer.isSelected()) {
                if (searchedPaper == null) {
                    throw new Exception("Stock not found Error: " + userNameProperty.getValue() + " has no stocks of type - " +
                            comboBoxChooseStock.getSelectionModel().getSelectedItem() + " at his possetion. Lmao.");
                } else {
                    if (enteredQuantity > searchedPaper.getIdleQuantity()) {
                        throw new Exception("Insufficient Quantity Error: " + userNameProperty.getValue()
                                + " has only " + searchedPaper.getIdleQuantity() + " stocks" +
                                " Whilst has" + enteredQuantity + " idle shares of stock " + chosenStock + ". Lol."); //Insuffisient (not empty)
                    }
                }
            }
        }
        else{
            throw new Exception("Error: the quantity must me a none-negative number.");
        }
    }
    //throws exception in case of illegal input
    private InstructionDTO gatherInstructionDTO() {
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

    public void setMainController(ApplicationControl applicationController) {
        this.appControl = applicationController;
    }


    private void loadResultTable(List<TransactionDTO> list) {
        colSharedPrice.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("quantity"));
        colTotal1.setCellValueFactory(new PropertyValueFactory<TransactionDTO, Integer>("totalPayment"));
        colTime.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("strTime"));
        colOfferer.setCellValueFactory(new PropertyValueFactory<TransactionDTO, String>("invokersName"));
        tableOperationResult.getItems().setAll(FXCollections.observableArrayList(list));
    }

    private void loadUserStocksTable(StocksTradeSystem system, String userName) {

        colUserSymbol.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("symbol"));
        colUserCompany.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, String>("companyName"));
        colUserPrice.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("currentPrice"));
        colIdle.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("idleQuantity"));
        colAtMarket.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("atMarketQuantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<UserDTO.StockPaperDTO, Integer>("totalQuantity"));
        userTableView.getItems().setAll(FXCollections.observableArrayList(system.getSafeUser(userName).getOwnedStocks()));
    }

    public void wiringXMLtoTab(StocksTradeSystem system, String userName) throws Exception {
        setForm(system);
        userNameProperty.setValue(userName);
        userValueProperty.setValue(Integer.toString(system.getUserTotalVal(userName)));
        loadUserStocksTable(system, userName);
    }

    private void setForm(StocksTradeSystem system) {
        comboBoxChooseStock.getItems().addAll(system.getSafeStocks().keySet());
    }


}
