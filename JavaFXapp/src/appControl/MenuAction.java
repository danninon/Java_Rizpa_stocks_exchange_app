package appControl;

import SystemEngine.StockTradingSystem;

import java.io.IOException;


import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public enum MenuAction {
    LOAD(){
        @Override
        public void execute() throws Exception {
        }

    },
    SHOW_SPECIFIC_STOCKS() {
        @Override
        public void execute() throws Exception {

        }
    },

    BUY_OR_SELL(){
        @Override
        public void execute() throws Exception {


        }
    },


    SERIALIZE(){
        @Override
        public void execute() throws IOException {
//            System.out.println("Enter full path to (create)/(save to) dat file. Creates/Overwrites depending on if file already exists.");
//            String address = loadedScanner.nextLine();
//            savedPathToReadFrom = address;
//            System.out.println("Saving...");
//            marketInterface.writeStocksToFile(address);
//            System.out.println("Success!");

        }

    },
    DESERIALIZE(){
        @Override
        public void execute() throws IOException, ClassNotFoundException {

//            if (savedPathToReadFrom != null) {
//                System.out.println("Reading file from address: " + savedPathToReadFrom);
//            }else {
//                System.out.println("You need to save the file first in order to do that.");
//                return;
//            }
//
//            System.out.println("Reading from saved file...");
//            marketInterface.readStocksFromFile();
//            systemLoaded = true;
//            System.out.println("Reading successful!");
        }


    };


    static ApplicationControl applicationControl;

    public static void setApplicationControl(ApplicationControl applicationControl){    MenuAction.applicationControl = applicationControl;}
    static StockTradingSystem marketManager;
    public static void setMarketManager(StockTradingSystem marketManager){ MenuAction.marketManager = marketManager;}

    public static MenuAction findAction(MenuAction currentAction) {
        for(MenuAction menuAction : MenuAction.values()) {
            if(menuAction.equals(currentAction))
                return menuAction;
        }
        return null;
    }


    public abstract void execute() throws Exception;

    final String XML = "1";
    final String Manual = "1";
    final String Automatic = "2";
    final String TESTER = "2";

    final String LOAD_ANS = "1";
    final String SHOW_ALL_STOCKS_ANS = "2";
    final String SHOW_SPECIFIC_STOCKS_ANS = "3";
    final String BUY_OR_SELL_ANS = "4";
    final String SHOW_ACTIVE_AUCTIONS_ANS = "5";
    final String SERIALIZE_ANS = "6";
    final String DE_SERIALIZE_ANS = "7";
    final String QUIT_ANS = "8";

    final String BUY_STOCKS = "1";
    final String SELL_STOCKS = "2";
    final String MAIN_MENU = "3";

    final String MKT = "2";
    final String NOT_INIT = "0";


    static String savedPathToReadFrom = null;
    final static String readFrom = "SystemEngine/src/resources/readFile.dat";
    final static String writeTo = "SystemEngine/src/resources/readFile.dat";
    final static String XML_FILE_NAME1 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex1-small.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME2 = "SystemEngine/src/resources/ex1-error-3.2.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME3 = "SystemEngine/src/resources/ex1-error-3.3.xml"; //C:/Users/Z490/RSE/

}