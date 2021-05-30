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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MarketManager implements StocksTradeSystem {

    private boolean x;


    //@Override
    public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstructionDTO, String enteredSymbol) throws Exception {
        //some invalid user inputs

        Instruction newInstruction = createInstructionFromDTO(newInstructionDTO);
        Stock searchedStock = stocks.get(enteredSymbol);
        CMD4ReturnBundle result = null;

        if (searchedStock != null) {
            result = operateOnMarket2(searchedStock, newInstruction, enteredSymbol);
        }
        else {
             throw new Exception("Couldn't find the required stock.");
            }
        return result;
    }

    //, Collection<Instruction> sameType, Collection<Instruction> oppositeType
    private CMD4ReturnBundle operateOnMarket2(Stock searchedStock, Instruction newInstruction, String searchedStockSymbol) {

        User instructionOperator = users.get(newInstruction.getOperatorName());

        //defaults to sell
        Collection<Instruction> sameTypeCollection = searchedStock.getSaleInstructionData();
        Collection<Instruction> oppositeInstructionCollection = searchedStock.getBuyInstructionData();

        //if buy
        if (newInstruction.isBuy()) {
            sameTypeCollection = searchedStock.getBuyInstructionData();
            oppositeInstructionCollection = searchedStock.getSaleInstructionData();
        }

        int totalGetTotalOperations = 0;
        for (Instruction oppositeInstruction : oppositeInstructionCollection) {
            if (!newInstruction.getOperatorName().equals(oppositeInstruction.getOperatorName())) {
                if (newInstruction.matchesOppositeInstruction(newInstruction)) {

                    Transaction tr = newInstruction.operateStock(oppositeInstruction);
                    searchedStock.getTransactionList().push(tr);
                    searchedStock.updateStock(tr.getQuantity(), tr.getPrice(), tr.getTotalPayment());
                    totalGetTotalOperations += tr.getQuantity();
                    User buyer = users.get(tr.getBuyersName());
                    User seller = users.get(tr.getSellersName());

                    int quantityTraded = tr.getQuantity();

                    buyer.updateUserAfterBuyingCase(searchedStockSymbol, quantityTraded);


                    if (seller.equals(instructionOperator)){
                        seller.updateUserAfterSellingCase(searchedStockSymbol, 0); //should remove paper incase 0 remain
                    }
                    else{
                        seller.updateUserAfterSellingCase(searchedStockSymbol, quantityTraded); //should remove paper incase 0 remain
                    }
                    if (oppositeInstruction.getQuantity() == 0) {
                        oppositeInstructionCollection.remove(oppositeInstruction);
                    }
                    if (newInstruction.getQuantity() == 0) {
                        break;
                    }
                }
            }
        }

        if (newInstruction.getQuantity() > 0) {
            if (!newInstruction.isBuy()) {
                instructionOperator.updateUserAfterInsertingSaleInstruction(searchedStockSymbol, newInstruction.getQuantity());
            }
            sameTypeCollection.add(newInstruction);
        }

        return new CMD4ReturnBundle(searchedStock.getTransactionList(), newInstruction);
    }


    private void updateUserInstructionCase(String searchedStockSymbol, int quantity) {
    }




//    private void updateUser(User operator, User buyer, User seller, Transaction tr, String symbol) {
//        User.transact( operator,  buyer,  seller,  tr,  symbol);
//
//    }
    //public CMD4ReturnBundle operateOnMarket1(Instruction newInstruction, String operatorsName, String enteredSymbol) throws Exception {

//    public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstructionDTO, String enteredSymbol) throws Exception {
//        String operatorName = newInstructionDTO.getOperatorName();
//
////        if (!newInstructionDTO.getIsBuy()) { //if sell command
////           if( getUser(operatorName).getStocksInBook().get(enteredSymbol) < newInstructionDTO.getQuantity()){
////               throw new IllegalArgumentException("The user: " + operatorName + " owns only" + getUser(operatorName).getStocksInBook().get(enteredSymbol) + "" +
////                       "stocks. and does not have the requested(" + newInstructionDTO.getQuantity() + ") amount of these stocks.");
////            }
////        }
//        Stock searchedCompany = stocks.get(enteredSymbol);
//        Instruction newInstruction = createInstructionFromDTO(newInstructionDTO);
//        CMD4ReturnBundle retVal;
//        if (searchedCompany != null) {
//            if (newInstruction.isBuy()) {
//                retVal = searchedCompany.operate(this, newInstruction, searchedCompany.getBuyInstructionData(), searchedCompany.getSaleInstructionData());
//            } else {
//                retVal = searchedCompany.perate(newInstruction, searchedCompany.getSaleInstructionData(), searchedCompany.getBuyInstructionData());
//            }
//            updateUsers(retVal, enteredSymbol, newInstruction);
//            return retVal;
//        }
//        else{
//            throw new IllegalArgumentException("The symbol you've entered does not exists within the system!\n" + "Going back to menu...");
//
//        }
//    }
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

    //this is always legal, throws b4 if not
//    private void updateUsers(CMD4ReturnBundle bundle, String symbol, Instruction newInstruction) throws Exception {
//        int totalQuantityOperated = 0;
//
//
//        for (TransactionDTO tr : bundle.getTransactionsMade()) {
//
//            User buyingUser = users.get(tr.getBuyerName());
//            User sellingUser = users.get(tr.getSellerName());
//
//            int quantityTraded = tr.getQuantity();
//
//                buyingUser.updateUserAfterBuyingCase(symbol, quantityTraded);
//                sellingUser.getPaper(symbol).updateAfterSold(quantityTraded, symbol); //removes the paper if sold out
//            } else
//                {
//                buyingUser.getPaper(symbol).updateAfterBought(quantityTraded);
//                sellingUser.getPaper(symbol).updateAfterSold(quantityTraded, symbol); //removes the paper if sold out
//            }
//        }
//        }

//                    quantityTraded = -quantityTraded; }
//                int newQuantity = users.get(invokersName).getQuantity(symbol) + quantityTraded;
//                users.get(invokersName).setQuantity(symbol, newQuantity);
//
//                User invokingUser = users.get(invokersName);
//                if (invokingUser.getStocksInBook().get(symbol).equals(0)){
//                    users.get(invokersName).removePaper(symbol);
//                }

//                totalQuantityOperated += quantityTraded;
//                //delete 0 values
//            }
//
//        String operatorsName = newInstruction.getInvokersName();

//        users.get(operatorsName).setQuantity(symbol, users.get(operatorsName).getQuantity(symbol) + totalQuantityOperated);




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
       User user = users.get(userName);
       for (String symbol: user.getStocksInBook().keySet()){
           sum = (stocks.get(symbol).getPrice() * users.get(userName).getStocksInBook().get(symbol).getTotalAmount()) + sum;
       }
       return sum;
    }

    public int getQuantityOfStockByUser(String userName, String symbol) throws Exception {
        return users.get(userName).getPaper(symbol).getTotalAmount();
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
            User newUser = new User();
            if (newConvertedUsers.get(user.getName()) == null) {

                for (RseItem item : user.getRseHoldings().getRseItem()) {
                    if (stocks.containsKey(item.getSymbol())) { //cannot add papers yet
                        newUser.addPaper(item.getSymbol(), item.getQuantity());
                    } else
                        throw new Exception("Error! the user " + user.getName() + "contains the symbol: " + item.getSymbol() + "which is unknown to the system."); //make ExceptionSymbolNotFound
                }
                newConvertedUsers.put(user.getName(), newUser);
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
            int stockOwnedByUserOfGivenSymbol = users.get(userName).getPaper(symbol).getTotalAmount();
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

