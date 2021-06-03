package SystemEngine;


import DTO.*;
import SystemEngine.Exceptions.UniqueException;
import SystemEngine.Instruction.Instruction;
import SystemEngine.Instruction.LMT;
import SystemEngine.Instruction.MKT;
import SystemEngine.generated.*;
import SystemEngine.tasks.FileLoaderTask;
import appControl.ApplicationControl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class MarketManager implements StockTradingSystem {

    private boolean x;


    //@Override
    public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstructionDTO, String enteredSymbol) throws Exception {
        //some invalid user inputs

        Instruction newInstruction = createInstructionFromDTO(newInstructionDTO);
        Stock stock = stocks.get(enteredSymbol);
        CMD4ReturnBundle result = null;

        if (stock != null) {
            result = operateOnMarket2(stock, newInstruction, enteredSymbol);
        }
        else {
             throw new Exception("Couldn't find the required stock.");
            }
        return result;
    }

    //, Collection<Instruction> sameType, Collection<Instruction> oppositeType
    private CMD4ReturnBundle operateOnMarket2(Stock stock, Instruction newInstruction, String searchedStockSymbol) throws InterruptedException {

        User instructionOperator = users.get(newInstruction.getOperatorName());

        //defaults to sell
        Collection<Instruction> sameTypeCollection = stock.getSaleInstructionData();
        Collection<Instruction> oppositeInstructionCollection = stock.getBuyInstructionData();
        LinkedList<Transaction> newTransactionsMade = new LinkedList();
        //if buy
        if (newInstruction.isBuy()) {
            sameTypeCollection = stock.getBuyInstructionData();
            oppositeInstructionCollection = stock.getSaleInstructionData();
        }

        for (Instruction oppositeInstruction : oppositeInstructionCollection) {
            if (!newInstruction.getOperatorName().equals(oppositeInstruction.getOperatorName())) {
                if (newInstruction.matchesOppositeInstruction(oppositeInstruction)) {


                    //updates the transaction
                    Transaction tr = newInstruction.operateStock(oppositeInstruction);
                    stock.getTransactionList().push(tr);
                    newTransactionsMade.push(tr);

                    //updates the stock
                    stock.updateStock(tr.getQuantity(), tr.getPrice(), tr.getTotalPayment());
                    User buyer = users.get(tr.getBuyersName());
                    User seller = users.get(tr.getSellersName());
                    int quantityTraded = tr.getQuantity();

                    //updates the users
                    buyer.updateBuyer(searchedStockSymbol, quantityTraded);
                    if (seller.equals(instructionOperator))
                    { seller.updateSeller(true, searchedStockSymbol, quantityTraded); }
                        else
                            { seller.updateSeller(false, searchedStockSymbol, quantityTraded); }
                    }
                    //finish condition
                    if (newInstruction.getQuantity() == 0) {
                        break;
                    }
                }
            }

        //saves priceTimeData
        stock.savePriceTimeVector(stock.getPrice(), newInstruction.getTime());


        //creating new instruction if not finished
        if (newInstruction.getQuantity() > 0) {
            if (!newInstruction.isBuy()) {
                instructionOperator.updateUserAfterInsertingSaleInstruction(searchedStockSymbol, newInstruction.getQuantity());
            }
            sameTypeCollection.add(newInstruction);
        }

        //case not transactions
        if (newTransactionsMade.size() == 0){
            newTransactionsMade = null;
            newInstruction.setPriceAfterNoTransaction(stock.getPrice());
        }

        //case no new instruction
        if (newInstruction.getQuantity() == 0) {
            newInstruction = null;
        }

        //deletes empty buy\sell instructions
        clearCache(searchedStockSymbol);

        return new CMD4ReturnBundle(newTransactionsMade, newInstruction);
    }

    private void update(int price) {

    }

    public void clearCache(String searchedStockSymbol) throws InterruptedException {
        closeFinishedInstructions(stocks.get(searchedStockSymbol));
    }

    public void closeFinishedInstructions(Stock stock) throws InterruptedException {

        Iterator<Instruction> itr1 = stock.getBuyInstructionData().iterator();
        while(itr1.hasNext()) {
            if (itr1.next().getQuantity() == 0)
                itr1.remove();
        }
        Iterator<Instruction> itr2 = stock.getSaleInstructionData().iterator();
        while(itr2.hasNext()) {
            if (itr2.next().getQuantity() == 0)
                itr2.remove();
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


    public Instruction createMatchingInstruction(String instructionType, LocalDateTime time, boolean isBuy, String symbol, int price, int quantity, String operatorName) throws Exception {
        if (instructionType.equals(caseLMT))
            return new LMT(time, isBuy, price, quantity, operatorName);
        else if (instructionType.equals(caseMKT)) {

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

    @Override
    public boolean loadXML(String xmlFileName) throws Exception {
        File file = new File(xmlFileName);
        InputStream inputStream = new FileInputStream(new File(xmlFileName)); //read from user

        JAXBContext jc = JAXBContext.newInstance(JAXB_MARKET_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        RizpaStockExchangeDescriptor marketUnref = (RizpaStockExchangeDescriptor) u.unmarshal(inputStream);
        return true;
    }

    public int getQuantityOfStockByUser(String userName, String symbol) throws Exception {
        return users.get(userName).getPaper(symbol).getTotalAmount();
    }



    public Map<String, StockDTO> getSafeStocks() {
        return new MarketDTO(stocks).getSafeStocks();
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

    public void loadFile(String xmlFileName, int sleepTime, Runnable onFinish, Consumer<String> exceptionHandling, ApplicationControl contoller) throws  InterruptedException
    {
       RizpaStockExchangeDescriptor[] marketUnref = {new RizpaStockExchangeDescriptor()};


        Consumer<Integer> loadFile = (x) -> {
            try {
                File file = new File(xmlFileName);
                InputStream inputStream = new FileInputStream(new File(xmlFileName)); //read from user

                JAXBContext jc = JAXBContext.newInstance(JAXB_MARKET_PACKAGE_NAME);
                Unmarshaller u = jc.createUnmarshaller();
                marketUnref[0] = (RizpaStockExchangeDescriptor) u.unmarshal(inputStream);

            } catch (Exception e) {
                e.printStackTrace();
            }};

            Consumer<Integer> updateStocksList = (x) -> {
                try {
                    RseStocks companiesUnref = marketUnref[0].getRseStocks();
                    stocks.putAll(translateRseStocks(companiesUnref));


                } catch (Exception e) {
                    e.printStackTrace();
                }};

            Consumer<Integer> updateUsersList = (x) -> {
                try {
                    RseUsers usersUnref = marketUnref[0].getRseUsers();
                    users.putAll(translateRseUsers(usersUnref));


                } catch (Exception e) {
                    e.printStackTrace();
                }};

        FileLoaderTask currentRunningTask=new FileLoaderTask(xmlFileName,sleepTime,loadFile,updateStocksList,updateUsersList,exceptionHandling);

        contoller.bindTaskToUIComponents(currentRunningTask,onFinish);

        new Thread(currentRunningTask).start();

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
            Stock addedStockTradingSystem = new Stock(stock.getRseCompanyName(), stock.getRsePrice()); //throws if not by format of name, symbol or price., toUpper was instructed by Aviad.
            checkDoubles(dataToBeChecked, stock);

            if (dataToBeChecked.containsKey(stock.getRseSymbol()))
                throw new UniqueException("Symbol", stock.getRseSymbol());
            dataToBeChecked.put(stock.getRseSymbol(), addedStockTradingSystem);
        }
        return dataToBeChecked;
    }

    private void checkDoubles(Map<String, Stock> dataArr, RseStock newData) throws UniqueException {

        for (Stock stock : dataArr.values()) {
            if (stock.getCompanyName().equals(newData.getRseCompanyName()))
                throw new UniqueException("company's name", stock.getCompanyName());
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

        Stock stock = new Stock("Google", 100);

    }

    @Override
    public void prepNextAction() throws InterruptedException {

    }
    final static String readFrom = "SystemEngine/src/resources/readFile.dat";
    final static String writeTo = "SystemEngine/src/resources/readFile.dat";
    final static String XML_FILE_NAME1 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-small.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME2 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-error-2.1.xml"; //C:/Users/Z490/RSE/
    final static String XML_FILE_NAME3 = "C:\\Users\\Z490\\IdeaProjects\\RSE_PT1\\SystemEngine\\src\\resources\\ex2-error-2.2.xml"; //C:/Users/Z490/RSE/


    public Stock getStock(String latestSelectedStock) {
        return stocks.get(latestSelectedStock);
    }
}

