package appControl;

import DTO.*;
import SystemEngine.MarketManager;
import SystemEngine.StocksTradeSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import menuScreen.MenuScreenController;
import usersTabPane.UsersController;
import usersTabPane.adminTab.AdminTabController;
import usersTabPane.singleUserTab.SingleUserTabController;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplicationControl {
    final String SINGLE_USER_TAB_FXML_RESOURCE = "../usersTabPane/singleUserTab/singleUserTab2.fxml";
    final String ADMIN_TAB_FXML_RESOURCE = "../usersTabPane/adminTab/admin.fxml";

    @FXML
    private TabPane bodyComponent;

    @FXML
    private AnchorPane topComponent;
    @FXML
    private MenuScreenController topComponentController;

    @FXML
    private TextArea textAreaInformingUser;

    private final MarketManager marketManager;
    private Stage primaryStage;
    private MessengerArchitect messenger;
    StringProperty activeStockInBook = null;
    StringProperty textUserInformationProperty;
    List<UserAction> userActionLog;

    private boolean systemBootFinish;
    private boolean XMLloadedSuccessfully;

    private int actionCounter;
    StringProperty currentTime;
    private LocalDateTime getTimeMark(){return LocalDateTime.now();}
    public List<UserAction> getMyEventList() { return userActionLog; }


    public class UserAction {
        public UserAction(int actionNum, boolean status, String details, LocalDateTime time) {
            this.actionNum = actionNum;
            this.status = status;
            this.details = details;
            this.time = time;
            this.strTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        }

        private final int actionNum;
        private final boolean status;
        private final String details;
        private final String strTime;
        private final LocalDateTime time;

        public boolean isStatus() {
            return status;
        }

        public String getdetails() {
            return details;
        }

        public String getstrTime() {
            return strTime;
        }

        public LocalDateTime gettime() {
            return time;
        }

        public int getactionNum() {
            return actionNum;
        }
    }
    private void userActionViaTextUserInformation(boolean status, ActionEvent event){
//       if (systemBootFinish) {
//           if (status) {
               reloadTabsToTabsPane(event);
//           }
//       }

        userActionLog.add(new UserAction(actionCounter, status, textUserInformationProperty.getValue(), getTimeMark()));
        bodyComponentController.getAdminController().updateAdminLog();
        ++actionCounter;
    }

    private void userActionViaGivenString(boolean status, String msg, ActionEvent event) {
        userActionLog.add(new UserAction(actionCounter, status, msg, getTimeMark()));
        ++actionCounter;

        if (status) {
            reloadTabsToTabsPane(event);
        }


    }

    public ApplicationControl() {
        LocalDateTime timeMark = LocalDateTime.now();
        System.out.println("in ApplicationControl ctor");
        actionCounter = 1;

        XMLloadedSuccessfully = false;
        systemBootFinish = false;

        marketManager = new MarketManager();
        messenger = new MessengerArchitect();
        currentTime = new SimpleStringProperty(timeMark.format((DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))));
        textUserInformationProperty = new SimpleStringProperty(messenger.openingMessage());
        userActionLog = new ArrayList<>();

        MenuAction.setApplicationControl(this);
        MenuAction.setMarketManager(marketManager);
    }

//    private void userActionViaTextUserInformation(UserAction action) {
//        reloadTabsToTabsPane();
//        actionCounter++;
//        userActionLog.add(action);
//    }



    public void initialize() {
        System.out.println("in ApplicationControl initialize()");
        if (bodyComponentController != null && topComponentController != null) {
            topComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

        }
        textAreaInformingUser.textProperty().bind(textUserInformationProperty);
        userActionViaTextUserInformation(true, null);

    }

    public boolean loadXMLFile(String path, ActionEvent event) {
        LocalDateTime timeMark = LocalDateTime.now();
        boolean status = false;
        try {
            marketManager.loadXML(path);
            textUserInformationProperty.setValue("File loaded successfully.");
            systemBootFinish = true;
            status = true;
            userActionViaTextUserInformation(true, null);
        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), null);


        }
        XMLloadedSuccessfully = true;
        return status;
    }
    @FXML
    private UsersController bodyComponentController;

    private void reloadTabsToTabsPane(ActionEvent event) {
        try {
            String currentlySelectedTabName = null;
            String currentSelectedStock = null;
            if (XMLloadedSuccessfully) {
                currentlySelectedTabName = bodyComponentController.getOpenTab();
                currentSelectedStock = bodyComponentController.getOpenStockAtAdmin();
            }
            bodyComponent.getTabs().clear();
            addAdminTab(currentSelectedStock, event);
            createUserTabs(new ArrayList<SingleUserTabController>());

            if (XMLloadedSuccessfully) {
                for (Tab tab : bodyComponent.getTabs()) {
                    if (tab.getText().equals(currentlySelectedTabName)) {
                        bodyComponent.setSelectionModel(tab.getTabPane().getSelectionModel());
                        break;
                    }
                }
            }


        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), event);
        }

    }

    public StocksTradeSystem getMarketManager() {
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

        bodyComponent.getTabs().add(adminTab);
        adminTab.setText("Admin Tab");

    }




    public List<SingleUserTabController> createUserTabs(List<SingleUserTabController> tabList) throws Exception {

//        int savedSelectedIndex = bodyComponent.getSelectionModel().getSelectedIndex();
        // bodyComponent.getTabs().clear();

        for (String key : this.marketManager.getUsers().keySet()) {
            int ctr = 0;
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(SINGLE_USER_TAB_FXML_RESOURCE);
            loader.setLocation(url);
            Tab userTab = loader.load();
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


    public void TradeCommit(InstructionDTO gatherInstructionDTO, String symbol, boolean buySelected, ActionEvent event) {
        try {

            CMD4ReturnBundle bundle = marketManager.operateOnMarket1(gatherInstructionDTO, symbol);
            String msg = messenger.matchingActionMSG(bundle, buySelected);
            textUserInformationProperty.setValue(msg);
            userActionViaTextUserInformation(true, event);

    } catch (Exception e)
        {
           textUserInformationProperty.setValue(e.getMessage());
            userActionViaGivenString(false, e.getMessage(), event);
        }
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


}





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