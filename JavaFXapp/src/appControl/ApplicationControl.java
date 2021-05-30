package appControl;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.StockDTO;
import DTO.UserDTO;
import SystemEngine.MarketManager;
import SystemEngine.StocksTradeSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import menuScreen.MenuScreenController;
import users.singleUserTab.SingleUserTabController;
import users.usersController;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ApplicationControl {
    final String SINGLE_USER_TAB_FXML_RESOURCE = "../users/singleUserTab/singleUserTab2.fxml";
    @FXML
    private TabPane bodyComponent;

    @FXML private AnchorPane topComponent;
    @FXML private MenuScreenController topComponentController;

    @FXML private TextArea textAreaInformingUser;

    StringProperty activeStockInBook = null;
    StringProperty textUserInformationProperty;

    public ApplicationControl() {
        System.out.println("in ApplicationControl ctor");
        marketManager = new MarketManager();

        textUserInformationProperty = new SimpleStringProperty();
    }

    public void initialize() {
        System.out.println("in ApplicationControl initialize()");
        if (bodyComponentController != null && topComponentController != null) {
            topComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

        }
        textAreaInformingUser.textProperty().bind(textUserInformationProperty);
    }

    public boolean loadXMLFile(String path) {
        boolean status = false;
        try {
            marketManager.loadXML(path);
            textUserInformationProperty.setValue("File loaded successfully.");
            loadCurrentUsersState();
            status = true;
        } catch (Exception e) {
            //textUserInformationProperty.setValue("There was a problem with the chosen xml. Please retry with a valid one.");
            textUserInformationProperty.setValue(e.getMessage());
        }
        return status;
    }
    @FXML
    private usersController bodyComponentController;

    private void loadCurrentUsersState() {
        try {

            createUserTabs(new ArrayList<SingleUserTabController>());

        } catch (Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
        }

    }

    public StocksTradeSystem getMarketManager() {
        return marketManager;
    }

    private final MarketManager marketManager;
    private Stage primaryStage;

    public List<SingleUserTabController> createUserTabs(List<SingleUserTabController> tabList) throws Exception {

        int savedSelectedIndex = bodyComponent.getSelectionModel().getSelectedIndex();
        bodyComponent.getTabs().clear();
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
            if (ctr==savedSelectedIndex)
                bodyComponent.setSelectionModel(userTab.getTabPane().getSelectionModel());
             //   savedSelectedTab = userTab;
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

    }


    public void TradeCommit(InstructionDTO gatherInstructionDTO, String symbol, boolean buySelected) {
        try {
            StockDTO fetchedStock = marketManager.getSafeStock(symbol);
            CMD4ReturnBundle bundle = marketManager.operateOnMarket1(gatherInstructionDTO, symbol);

                loadCurrentUsersState(); //Do: reloading


        String msg = StringArchitect.matchingActionMSG(bundle, buySelected);
        textUserInformationProperty.setValue(msg);

    } catch (Exception e)
        {
        textUserInformationProperty.setValue(e.getMessage());

        }
    }

    public UserDTO getUser(String userName) {
        return (marketManager.getSafeUser(userName));
    }

    public List<UserDTO.StockPaperDTO> getUserStocksBook(String userName) {
        return (marketManager.getSafeUser(userName).getOwnedStocks());
    }

    static class StringArchitect {
        static String matchingActionMSG(CMD4ReturnBundle bundle, boolean buySelected) {
            //  boolean isBuy = isCurrentlyBuying();
            String retMSG = "";
            if (bundle.getInsDTO() == null) { //instruction wasn't made

                if (buySelected) {
                    retMSG += "Perfect match found!\nSuccessfully acquired the full extent of the request. \n ";
                } else {
                    retMSG += "Perfect match found!\nSuccessfully sold the full extent of the request. \n ";
                }
            }//theres an instruction
              else if (bundle.getTransactionsMade() != null){ //transactions were made
                  if (bundle.getTransactionsMade().size() != 0) {
                      if (buySelected) {
                          retMSG += "Partially match found!\nSuccessfully bought some of the request.\n";
                          retMSG += "\nThis partial updateUserAfterBuyingCase instruction was been added to the market(the reminder after partially buying some of the stocks): \n";
                      } else {
                          retMSG += "Partially match found!\nSuccessfully sold some of the request.\n";
                          retMSG += "\nThis partial sale instruction was added to the market(the reminder after partially selling some of the stock): \n";
                      }
                  }
                retMSG += instructionTimePriceQuantity(bundle.getInsDTO()); //Do: doesn't print
                    }
                else { //new instruction and no transaction - no transaction has been made
                    if (buySelected) {
                        retMSG += "There are no active sale instruction that matches with your request.\n";
                        retMSG += "The full  instruction has been added to the market. \n";
                    }
                    else{
                        retMSG += "The full sale instruction that has been added to the market. \n";
                         retMSG += "There are no active instruction that matches with your request.\n";
                    }
                retMSG += instructionTimePriceQuantity(bundle.getInsDTO()); //Do: doesn't print
                }
                return retMSG;
            }
        }
        static public String instructionTimePriceQuantity(InstructionDTO ins) {
            return "Time - " + ins.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\nPrice - " + ins.getPrice() +
                    " NIS\nQuantity - " + ins.getQuantity() + "\nFor a total price of:" + ins.getQuantity()*ins.getPrice() + " NIS";
        }

        static public void updateTextProperty(StringProperty textUserInformationProperty, Exception e) {
            textUserInformationProperty.setValue(e.getMessage());
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