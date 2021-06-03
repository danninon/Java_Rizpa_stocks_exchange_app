package appControl;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.UserDTO;
import SystemEngine.MarketManager;
import SystemEngine.Stock;
import SystemEngine.StockTradingSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import menuScreen.MenuScreenController;
import usersTabPane.UsersController;
import usersTabPane.adminTab.AdminTabController;
import usersTabPane.singleUserTab.SingleUserTabController;

import javax.swing.*;
import javax.swing.text.StyleContext;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static javafx.application.Application.STYLESHEET_CASPIAN;

public class ApplicationControl {


    @FXML
    private TabPane bodyComponent;
    @FXML
    private AnchorPane topComponent;
    @FXML
    private MenuScreenController topComponentController;
    @FXML
    private TextArea textAreaInformingUser;


    final String SINGLE_USER_TAB_FXML_RESOURCE = "../usersTabPane/singleUserTab/singleUserTab3.fxml";
    final String ADMIN_TAB_FXML_RESOURCE = "../usersTabPane/adminTab/admin.fxml";

    private final MarketManager marketManager;
    private Stage primaryStage;
    private MessengerArchitect messenger;

    StringProperty textUserInformationProperty;
    private boolean systemBootFinish;

    private boolean XMLloadedStatus;
    private int actionCounter;
    StringProperty currentTimeProperty;
    StringProperty activeStockInBookProperty = null;
    private String latestSelectedTabName = null;
    private String latestSelectedStock = null;
    private CMD4ReturnBundle latestBundle;

    private AdminLog adminLog;


    private LocalDateTime getTimeMark() {
        return LocalDateTime.now();
    }

    public boolean isXMLloadedStatus() {
        return XMLloadedStatus;
    }


    public boolean getXmlLoadedStatus() {
        return XMLloadedStatus;
    }

    public CMD4ReturnBundle getLatestBundle() {
        return latestBundle;
    }

    @FXML
    public void loadXMLFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        Consumer<String> exceptionHandling = msg -> {
            textUserInformationProperty.setValue(msg);
        };

        int sleepTime = 2000;


        try {
            marketManager.loadFile(selectedFile.getPath(), sleepTime, () -> {
                        textUserInformationProperty.setValue("System loaded.");
                    },
                    exceptionHandling, this);

        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
        }
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        textUserInformationProperty.bind(aTask.messageProperty());


        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish), aTask.getValue());
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish, boolean taskSucceeded) {
        textUserInformationProperty.unbind();


        if (taskSucceeded) {
            XMLloadedStatus = true;
            userActionViaGivenString(true, "loaded", null);
            onFinish.ifPresent(Runnable::run);

        }
    }

    public void openAdminStock(ActionEvent event) {
//      dddddddmponentController.getAdminController().loadAdminStocks(marketManager, latestSelectedStock, event);
    }

    private void userActionViaGivenString(boolean status, String operationDetails, ActionEvent event) {
        adminLog.add(actionCounter, status, operationDetails, getTimeMark());
        ++actionCounter;
        if (status) {
            reloadTabsToTabsPane(event);
        }

        bodyComponentController.getAdminController().updateAdminLog();
    }

    public ApplicationControl() {
        LocalDateTime timeMark = LocalDateTime.now();
        System.out.println("in ApplicationControl ctor");
        actionCounter = 1;

        XMLloadedStatus = false;
        systemBootFinish = false;

        marketManager = new MarketManager();
        messenger = new MessengerArchitect();
        currentTimeProperty = new SimpleStringProperty(timeMark.format((DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))));
        textUserInformationProperty = new SimpleStringProperty("");
        adminLog = new AdminLog(this);
    }


    public void initialize() {

        textAreaInformingUser.textProperty().bind(textUserInformationProperty);

        System.out.println("in ApplicationControl initialize()");
        if (bodyComponentController != null && topComponentController != null) {
            topComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

        }

        String retString = systemCheck();
        retString += messenger.openingMessage();
        textUserInformationProperty.setValue(retString);

        userActionViaGivenString(systemBootFinish, adminLog.BOOT + " was successful.", null);


    }

    //assumes stockSymbol exists in the system because data was loaded from here
    public void newStockSearched(String stockSymbol, ActionEvent event) {
        if (stockSymbol != null) {
            textUserInformationProperty.setValue("Fetched stock: " + stockSymbol + "");
            bodyComponentController.getAdminController().setStockDetails(marketManager.getStock(stockSymbol).getCompanyName(), marketManager.getStock(stockSymbol).getPrice());
            userActionViaGivenString(systemBootFinish, adminLog.SEARCH + " " + stockSymbol + " was successful.", event);
        }

    }

    private String systemCheck() {
        boolean status = true;
        String msg = "Validating system...";

        msg += messenger.Crlf();
        //checkControllers
        if (bodyComponentController == null) {
            msg += "body Controller is Disconnected.";
            status = false;
        } else {
            msg += "Main tab Pane controller now connected.";
        }
        msg += messenger.Crlf();
        if (bodyComponent == null) {
            msg += "Main tabPane is disconnected.";
            ;
            status = false;
        } else {
            msg += "Main tabPane is now connected";
        }
        msg += messenger.Crlf();
        if (topComponentController == null) {
            msg += "Toolbar controller is disconnected.";
            status = false;
        } else {
            msg += "Toolbar controller is now connected.";
        }
        msg += messenger.Crlf();
        if (topComponent == null) {
            msg += "Toolbar is disconnected.";
            status = false;
        } else {
            msg += "Toolbar is now connected.";
        }
        msg += messenger.Crlf();
        msg += messenger.Crlf();
//        if (bodyComponentController != null) {
//            if (bodyComponentController.getAdminController() == null) {
//                msg += "UsersController could not find it's adminController";
//                status = false;
//            }
//            msg += "Admin control tab is now connected\n";
//        }
        systemBootFinish = status;

        if (systemBootFinish == false) {
            msg += "System booting has failed. please contact support.";
            status = false;
        } else {
            msg += "System booting was a great success! " +
                    "\n*(Read it like Borat would have said it)";
        }
        msg += messenger.Crlf();
        msg += messenger.Crlf();
        return msg;
    }

    public boolean loadXMLFile(String path, ActionEvent event) {
        LocalDateTime timeMark = LocalDateTime.now();
        boolean status = false;
        try {
            marketManager.loadXML(path);
            textUserInformationProperty.setValue("File loaded successfully.");
            systemBootFinish = true;
            status = true;
            userActionViaGivenString(true, adminLog.LOAD_XML + " was successful.", event);
            //userActionViaTextUserInformation(true, null);
        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), null);
        }
        XMLloadedStatus = true;
        return status;
    }

    @FXML
    private UsersController bodyComponentController;

    private void reloadTabsToTabsPane(ActionEvent event) {
        try {
            latestSelectedTabName = null;
            latestSelectedStock = null;
            if (XMLloadedStatus) {
                latestSelectedTabName = bodyComponentController.getOpenTab();
                latestSelectedStock = bodyComponentController.getOpenStockAtAdmin();
            }
            bodyComponent.getTabs().clear();
            addAdminTab(latestSelectedStock, event);

            List<SingleUserTabController> tabListControlllers = formAndAddNewUserTabs(new ArrayList<SingleUserTabController>(), latestSelectedTabName);
            bodyComponentController.setTabControllerList(tabListControlllers);

            if (XMLloadedStatus) {
                for (Tab tab : bodyComponent.getTabs()) {
                    if (tab.getText().equals(latestSelectedTabName)) {
                        bodyComponent.getSelectionModel().select(tab);
                        //bodyComponent.setSelectionModel(tab.getTabPane().getSelectionModel());
                        break;
                    }
                }
            }


        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), event);
        }

    }

    public StockTradingSystem getMarketManager() {
        return marketManager;
    }


    public void addAdminTab(String previouslySelectedStock, ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(ADMIN_TAB_FXML_RESOURCE);
        loader.setLocation(url);
        AnchorPane adminAP = loader.load();
        Tab adminTab = new Tab();
        adminTab.setContent(adminAP);
        bodyComponentController.setAdminTab(adminTab);

        AdminTabController adminController = loader.getController();
        adminController.setMainController(this);

        bodyComponentController.setAdminController(adminController);
        adminController.loadAdminStocks(marketManager, previouslySelectedStock, event);

        if (previouslySelectedStock != null) {
            adminController.setStockDetails(marketManager.getStock(previouslySelectedStock).getCompanyName(), marketManager.getStock(previouslySelectedStock).getPrice());
        }

        bodyComponent.getTabs().add(adminTab);

        if (getXmlLoadedStatus() == true) {
            if (bodyComponentController.getAdminController() != null) {
                if (bodyComponentController.getAdminController().getOpenStock() != null) {
                    bodyComponentController.getAdminController().updateFluctuationChart(event, marketManager.getSafeStock(bodyComponentController.getAdminController().getOpenStock()));
                }
            }
        }
        adminTab.setText("Admin Tab");

    }


    public List<SingleUserTabController> formAndAddNewUserTabs(List<SingleUserTabController> tabList, String currentlySelectedTabName) throws Exception {

//        int savedSelectedIndex = bodyComponent.getSelectionModel().getSelectedIndex();
        // bodyComponent.getTabs().clear();

        for (String key : this.marketManager.getUsers().keySet()) {

            int ctr = 0;
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(SINGLE_USER_TAB_FXML_RESOURCE);

            loader.setLocation(url);
            AnchorPane userAP = loader.load();
            Tab userTab = new Tab();
            userTab.setContent(userAP);

            //  Tab userTab = new Tab();
            // userTab.setContent(content);
            SingleUserTabController singleTabController = loader.getController();

            tabList.add(singleTabController);

            singleTabController.setMainController(this);
            singleTabController.setUser(this.marketManager.getSafeUser(key));
            userTab.setText(key);

            singleTabController.wiringXMLtoTab(marketManager, key);

            bodyComponent.getTabs().add(userTab);
//            if (ctr==savedSelectedIndex)
//                bodyComponent.setSelectionModel(userTab.getTabPane().getSelectionModel());
//             //   savedSelectedTab = userTab;
        }

        //  tabPaneUsers.setSelectionModel(savedSelectedIndex);
        return tabList;
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getStage() {
        return primaryStage;
    }

    public void throwMainApplication(Exception e) {
        textUserInformationProperty.setValue(e.getMessage());
        userActionViaGivenString(false, e.getMessage(), null);
    }


    public CMD4ReturnBundle tradeCommit(InstructionDTO gatherInstructionDTO, String symbol, boolean buySelected, ActionEvent event) {
        try {

            latestBundle = marketManager.operateOnMarket1(gatherInstructionDTO, symbol);

            String msg = messenger.matchingActionMSG(latestBundle, buySelected);

            textUserInformationProperty.setValue(msg);

            userActionViaGivenString(true, adminLog.TRADE + " on stock " + symbol + " successful.", event);
            SingleUserTabController controller = bodyComponentController.getCurrentUserController(latestSelectedTabName);

            controller.setSearchStockLabel(symbol);

            if (latestBundle.getInsDTO() != null) {
                controller.loadInstructionTab(latestBundle.getInsDTO());
            }

            if (latestBundle.getTransactionsMade() != null) {
                controller.getTranscationTable().getItems().clear();
                controller.loadTransactionsTab(latestBundle.getTransactionsMade());
            }


        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), event);
        }
        return latestBundle;
    }

    public UserDTO getUser(String userName) {
        return (marketManager.getSafeUser(userName));
    }

    public List<UserDTO.StockPaperDTO> getUserStocksBook(String userName) {
        return (marketManager.getSafeUser(userName).getOwnedStocks());
    }

    public boolean systemBootFinish() {
        return systemBootFinish;
    }


    public void testInputFunc(ActionEvent event) throws Exception {
        loadXMLFile(XML_FILE_NAME1, event);

    }

    final static String XML_FILE_NAME1 = "C:\\ex2-small.xml"; //C:/Users/Z490/RSE/

    public List<AdminLog.AdminAction> getAdminLog() {
        return adminLog.getMyEventList();
    }

    public void changeCSS_Seq(ActionEvent event, StringProperty currentCSSProperty) {
        ++seq_counter;
        ObservableList<String> sheets = primaryStage.getScene().getStylesheets();
        sheets.clear();
        String into = "Currently CCSS loaded: ";
        if (seq_counter == MODANA){

            currentCSSProperty.setValue(into+"No Style - Style");
        }
        else if (seq_counter == GOLDEN_TIGER ){
            currentCSSProperty.setValue(into+"Goldien Tiger(Recommanded)");
            sheets.add(GOLDEN_TIGER_STYLE);
        }
        else if (seq_counter == GREEN_LIZARD){
            currentCSSProperty.setValue(into+"Green lizard(Nope)");
            sheets.add(GREEN_LIZARD_STYLE);
            seq_counter = 0;
        }


    }

    int seq_counter = 1;


final int MODANA = 1;
final String MODANA_STYLE = null;
final int GOLDEN_TIGER = 2;
final String GOLDEN_TIGER_STYLE = "resource/CSS/testCSS2.css";
final int GREEN_LIZARD = 3;
final String GREEN_LIZARD_STYLE = "resource/CSS/testCSS3.css";
final int SHARP = 4;
final String SHARP_STYLE = "resource/CSS/aerofx-master/aerofx-master/aerofx/src/main/resources/org/aerofx/win7.css";


}
//public InstructionDTO(Instruction originalIns) {
//        this.quantity = originalIns.getQuantity();
//        this.time = originalIns.getTime();
//        this.price = originalIns.getPrice();
//        this.isNew = originalIns.checkIfNew();
//        this.isBuy = originalIns.isBuy();
//        this.instructionType = originalIns.getClass().getSimpleName();
//        this.operatorName = originalIns.getInvokersName();
//        this.strTime = originalIns.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
//    }
//  public String getOperatorName(){return operatorName; }
//    Instruction(LocalDateTime time, boolean isBuy, int price, int quantity, String operatorName) throws Exception {
//        this.time = time;
//        this.isBuy = isBuy;
//        this.price = price;
//        this.quantity = quantity;
//        this.operatorName = operatorName;
//        this.checkLegalInstruction();
//
//    }



//    public void updateStocksBookSearchedSymbol(String symbol) {
//    }

//    public void loadComponents() {
//
//    }


//   currentLeftComponentController = leftComponentController;
//   informConnections(leftComponentController,bodyComponentController);
//    private void informConnections(SubController controller, SubScreen screen) {
//        controller.know(screen);
//        screen.know(controller);
//    }
//    public void readXmlToEngine() throws IOException {
//        leftComponentController.submitData();
//        rightComponentController.submitData();
//        bottomComponentController.submitData();
//    }

//    public void requestUpdateTradeOutput(CMD4ReturnBundle bundle) {
//        bodyComponentController.updateTradeScreen(bundle);
//    }
//
//    public void requestUpdateStocksBook(String enteredSymbolText) throws Exception {
//        //if stock found
//        bodyComponentController.updateStocksBookSearchedSymbol(marketManager.getSafeStock(enteredSymbolText), enteredSymbolText);
//        //else error
//    }


// public void requestLoadUI(SubController.Type requested) throws IOException {
//        if (requested.equals(SubController.Type.STOCKS_BOOK)) {
//
//        } else if (requested.equals(SubController.Type.TRADE)) {
//
//
//        } else if (requested.equals(SubController.Type.INTRODUCTION)) {
//
//        }
//
//    }

//    public void requestLoadUI(SubController.Type requested) throws IOException {
//        if (requested.equals(SubController.Type.STOCKS_BOOK)) {
//            AnchorPane control = FXMLLoader.load(getClass().getResource(STOCKSBOOK_CONTROL_RESOURCE));
//            leftComponent.getChildren().setAll(control);
//            currentLeftComponentController = new StocksBookInputScreenController();
//
//        } else if (requested.equals(SubController.Type.TRADE)) {
//            AnchorPane newLeftControl = FXMLLoader.load(getClass().getResource("/stocksBookInputScreen.fxml"));
//
//          //  leftComponent.getChildren().setAll(control);
//            currentLeftComponentController = new TradeInputScreenController(leftComponent);
//
//        } else if (requested.equals(SubController.Type.INTRODUCTION)) {
//            AnchorPane blankControl = new AnchorPane();
//            //put money icon
//            blankControl.setPrefHeight(400);
//            blankControl.setPrefWidth(270);
//            leftComponent.getChildren().setAll(blankControl);
//            currentLeftComponentController = new StocksBookInputScreenController();
//            System.out.println("in introduction");
//
//        }
//        currentLeftComponentController.setMainController(this);
//        currentLeftComponentController.setManager(this.marketManager);
//        currentLeftComponentController.submitData();
//    }