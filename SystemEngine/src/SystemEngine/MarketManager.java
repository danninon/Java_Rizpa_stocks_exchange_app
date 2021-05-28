package SystemEngine;


import DTO.*;
import SystemEngine.Exceptions.UniqueException;
import SystemEngine.Instruction.Instruction;
import SystemEngine.Instruction.LMT;
import SystemEngine.Instruction.MKT;
import SystemEngine.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MarketManager implements StocksTradeSystem{

    private boolean x;



    public CMD4ReturnBundle operateOnStocks(Instruction newInstruction, String operatorsName, String enteredSymbol) throws Exception {

        //some invalid user inputs


        Stock searchedCompany = stocks.get(enteredSymbol);
        CMD4ReturnBundle retVal;


        if (searchedCompany != null) {
            if (newInstruction.isBuy()) {
                retVal = searchedCompany.operateStocks(newInstruction, searchedCompany.getBuyInstructionList(), searchedCompany.getSaleInstructionList());
            } else {
                retVal = searchedCompany.operateStocks(newInstruction, searchedCompany.getSaleInstructionList(), searchedCompany.getBuyInstructionList());
            }
            updateUsers(retVal, enteredSymbol, newInstruction);
            return retVal;
        }
        else{
            throw new IllegalArgumentException("The symbol you've entered does not exists within the system!\n" + "Going back to menu...");

        }
    }

    @Override
    public CMD4ReturnBundle operateOnStocks(InstructionDTO newInstructionDTO, String enteredSymbol) throws Exception {
        String operatorName = newInstructionDTO.getOperatorName();
        //Do: if zero quantity was requested
        //Do: if 0 price was requested
//        if (!newInstructionDTO.getIsBuy()) { //if sell command
//           if( getUser(operatorName).getStocksInBook().get(enteredSymbol) < newInstructionDTO.getQuantity()){
//               throw new IllegalArgumentException("The user: " + operatorName + " owns only" + getUser(operatorName).getStocksInBook().get(enteredSymbol) + "" +
//                       "stocks. and does not have the requested(" + newInstructionDTO.getQuantity() + ") amount of these stocks.");
//            }
//        }
        Stock searchedCompany = stocks.get(enteredSymbol);
        Instruction newInstruction = createInstructionFromDTO(newInstructionDTO);
        CMD4ReturnBundle retVal;
        if (searchedCompany != null) {
            if (newInstruction.isBuy()) {
                retVal = searchedCompany.operateStocks(newInstruction, searchedCompany.getBuyInstructionList(), searchedCompany.getSaleInstructionList());
            } else {
                retVal = searchedCompany.operateStocks(newInstruction, searchedCompany.getSaleInstructionList(), searchedCompany.getBuyInstructionList());
            }
            updateUsers(retVal, enteredSymbol, newInstruction);
            return retVal;
        }
        else{
            throw new IllegalArgumentException("The symbol you've entered does not exists within the system!\n" + "Going back to menu...");

        }
    }
    //assumes userName always exists
    //Do exception if not found
    public UserDTO getSafeUser(String operatorName) {
        for(String userName: users.keySet()){
            if (userName.equals(operatorName))
                return new UserDTO(users.get(userName), this);
        }
        return null;
    }

    public User getUser(String operatorName) {
        for(String userName: users.keySet()){
            if (userName.equals(operatorName))
                return users.get(userName);
        }
        return null;
    }

    private Instruction createInstructionFromDTO(InstructionDTO newInstructionDTO) throws Exception {
        Instruction createdInstruction;
        if (newInstructionDTO.getInstructionType().equals("LMT")) {
            createdInstruction = new LMT(newInstructionDTO.getTime(), newInstructionDTO.getIsBuy(), newInstructionDTO.getPrice(), newInstructionDTO.getQuantity(), newInstructionDTO.getOperatorName());
        } else { //it's MKT
            createdInstruction = new MKT(newInstructionDTO.getTime(), newInstructionDTO.getIsBuy(), newInstructionDTO.getQuantity(), newInstructionDTO.getOperatorName());
        }
        return createdInstruction;
    }

    private void updateUsers(CMD4ReturnBundle bundle, String symbol, Instruction newInstruction) throws Exception {
        int totalQuantityOperated = 0;

            for (TransactionDTO tr : bundle.getTransactionsMade()) {
                String name = tr.getInvokedName();
                int quantityByDir = tr.getQuantity();
                if (!newInstruction.isBuy()) { quantityByDir = -tr.getQuantity(); }
                int newVal = users.get(name).getQuantity(symbol) - quantityByDir;
                users.get(name).setQuantity(symbol, newVal);
                totalQuantityOperated += quantityByDir;
                //delete 0 values
            }

        String operatorsName = newInstruction.getInvokersName();
        users.get(operatorsName).setQuantity(symbol, users.get(operatorsName).getQuantity(symbol) + totalQuantityOperated);
    }


    public Instruction createMatchingInstruction(String instructionType, LocalDateTime time, boolean isBuy, String symbol, int price, int quantity, String operatorName) throws Exception {
        if (instructionType.equals(caseLMT))
            return new LMT(time, isBuy, price, quantity, operatorName);
        else if (instructionType.equals(caseMKT)) {

//            return setAndRetMKT(new MKT(time, isBuy, quantity), symbol); //if no found sets price to NOT_INIT(0)
            return new MKT(time, isBuy, quantity, operatorName);
        } else {
            throw new UnsupportedOperationException("You've entered " + instructionType + "Which is an unknown input to the program, please try again");
        }
    }

    //returns index of company by symbol. also knows to return NOT_FOUND
    public StockDTO getSafeStock(String symbol) throws IllegalArgumentException {
        if (stocks.containsKey(symbol)) {
            return new StockDTO(stocks.get(symbol));
        }
        else
            throw new IllegalArgumentException("The symbol you've entered does not exists within the system!\n" + "Going back to menu...");
    }


    @Override
    public boolean userExist(String userName){
        return users.get(userName) != null;
    }

    @Override
    public int getUserTotalVal(String userName) throws Exception {
        int sum = 0;
       User book = users.get(userName);
       for (String symbol: book.getStocksInBook().keySet()){
           sum = stocks.get(symbol).getPrice()*users.get(userName).getQuantity(symbol)+ sum;
       }
       return sum;
    }

    public int getQuantityOfStockByUser(String userName, String symbol) throws Exception {
        return users.get(userName).getQuantity(symbol);
    }

    @Override
    public StocksBookDTO getStocksBook(String userName) throws IllegalArgumentException {
        User book = users.get(userName);
        if (book != null) {
            return new StocksBookDTO(book);
        }
        else{
            throw new IllegalArgumentException("The user: " + userName + "cannot be found in the system!");
        }
    }

    public Map<String, StockDTO> getSafeStocks() {
        return new MarketDTO(stocks).getSafeStocks();
    }

    public void prepNextAction() throws InterruptedException {

        for (Stock stock : stocks.values()) {
            stock.clearCache();
        }
        for (Stock stock : stocks.values()) {
            for (Transaction tr : stock.getTransactionList())
                tr.setIsNew(false);
        }
        TimeUnit.MILLISECONDS.sleep(1);
    }

    @Override
    public Map<String, User> getUsers() {
       return users;
    }

    @Override
    public void checkLegalSymbol(String symbol, String userName) throws Exception {
        if (stocks.get(symbol) == null){
            throw new Exception("ERROR!\nThe entered symbol: " + symbol + " doesn't exist!");

        }
        if (!(users.get(userName).symbolExistsInBook(symbol))) {
            throw new Exception("ERROR!\nThe user: " + userName + " doesn't own stock: " + symbol);
        }
    }

    //would be nicer if this returned boolean
    public boolean loadXML(String xmlFileName) throws Exception {
        File file = new File(xmlFileName);
        InputStream inputStream = new FileInputStream(new File(xmlFileName)); //read from user

        JAXBContext jc = JAXBContext.newInstance(JAXB_MARKET_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        RizpaStockExchangeDescriptor marketUnref = (RizpaStockExchangeDescriptor) u.unmarshal(inputStream);

        RseStocks companiesUnref = marketUnref.getRseStocks();
        stocks.putAll(translateRseStocks(companiesUnref));
        RseUsers usersUnref = marketUnref.getRseUsers();
        users.putAll(translateRseUsers(usersUnref));
        return true;
    }

    public Map<String, User> translateRseUsers(RseUsers fromStocks) throws Exception {

        List<RseUser> xmlUsers = fromStocks.getRseUser();
        Map<String, User> newConvertedUsers = new HashMap<>(); //name
        for (RseUser user : xmlUsers) {
            User newBook = new User();
            if (newConvertedUsers.get(user.getName()) == null) {

                for (RseItem item : user.getRseHoldings().getRseItem()) {
                    if (stocks.containsKey(item.getSymbol())) { //cannot add papers yet
                        newBook.addPaper(item.getSymbol(), item.getQuantity());
                    } else
                        throw new Exception("Error! the user " + user.getName() + "contains the symbol: " + item.getSymbol() + "which is unknown to the system."); //make ExceptionSymbolNotFound
                }
                newConvertedUsers.put(user.getName(), newBook);
            }
            else { throw new UniqueException("User Name", user.getName()); }

        }
        return newConvertedUsers;
    }

    public Map<String, Stock> translateRseStocks(RseStocks fromStocks) throws Exception {
        List<RseStock> xmlStocks = fromStocks.getRseStock();
        Map<String, Stock> dataToBeChecked = new HashMap<>();
        for (RseStock stock : xmlStocks) {
            Stock addedStock = new Stock(stock.getRseCompanyName(), stock.getRsePrice()); //throws if not by format of name, symbol or price., toUpper was instructed by Aviad.
            checkDoubles(dataToBeChecked, stock);

            if (dataToBeChecked.containsKey(stock.getRseSymbol()))
                throw new UniqueException("Symbol", stock.getRseSymbol());
            dataToBeChecked.put(stock.getRseSymbol(), addedStock);
        }
        return dataToBeChecked;
    }

    private void checkDoubles(Map<String, Stock> dataArr, RseStock newData) throws UniqueException {

        for (Stock stockInArr : dataArr.values()) {
            if (stockInArr.getCompanyName().equals(newData.getRseCompanyName()))
                throw new UniqueException("company's name", stockInArr.getCompanyName());
        }
    }

    public void readStocksFromFile(String readFileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(readFileName))) {
           stocks =
                    (Map<String, Stock>) in.readObject();
        }
    }
    public void writeStocksToFile(String fileName) throws IOException {

        File file = new File(fileName);
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {

            out.writeObject(stocks);
            out.flush();
        }
    }

    public void cleanStocks() {
        stocks.clear();
    }

    @Override
    public void cleanUsers() { users.clear();  }


    public void checkLegalQuantity(int quantity, String userName, String symbol, boolean isBuy) throws Exception {
        if (quantity <= 0){
            throw new IllegalArgumentException("quantity must be a positive number. " + "price entered: " + quantity);
        }
        if (!isBuy) {
            int stockOwnedByUserOfGivenSymbol = users.get(userName).getQuantity(symbol);
                if (stockOwnedByUserOfGivenSymbol < quantity) {
                    throw new IllegalArgumentException("The user: " + userName + " has asked to sell: " +
                            quantity + " stocks of Symbol: " + symbol + ".But has only: " + stockOwnedByUserOfGivenSymbol + " in possetion."); //make insufficient stocks exception
                }
        }
    }

    public void checkLegalPrice(int price) throws IllegalArgumentException {
        if (price < 0){
            throw new IllegalArgumentException("A stock's price must a none-negative number. " + "price entered: " + price);
        }
    }



    public void checkLegalInstructionType(String instructionType) throws UnsupportedOperationException {
        if (!instructionType.equals(caseLMT) && !instructionType.equals(caseMKT)) {
            throw new UnsupportedOperationException("Rizpa only supports LMT and MKT commands for now." + "Instruction entered:" + instructionType);
        }
    }


    final static String JAXB_MARKET_PACKAGE_NAME = "SystemEngine.generated";

    private Map<String, Stock> stocks = new HashMap<>();
    private Map<String, User> users = new HashMap<>();


    final String caseLMT = "1";
    final String caseMKT = "2";

    public void loadDataForTesting() throws Exception {

        Stock stock1 = new Stock("Google", 100);

    }


    final static String readFrom = "SystemEngine/src/resources/readFile.dat";
    final static String writeTo = "SystemEngine/src/resources/readFile.dat";
    final static String XML_FILE_NAME1 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-small.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME2 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-error-2.1.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME3 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-error-2.2.xml"; //C:/Users/Z490/RSE/


}

