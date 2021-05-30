package SystemEngine;

import DTO.*;
import SystemEngine.Instruction.Instruction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface StocksTradeSystem {
    public StockDTO getSafeStock(String symbol) throws Exception;

    public Map<String, StockDTO> getSafeStocks();

   // public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstructionDTO, String operatorsName, String enteredSymbol) throws Exception

    //public CMD4ReturnBundle operateOnMarket1(Instruction newInstruction, String operatorsName, String enteredSymbol) throws Exception;

    //public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstruction, String enteredSymbol) throws Exception;

    public CMD4ReturnBundle operateOnMarket1(InstructionDTO newInstructionDTO, String enteredSymbol) throws Exception;


    public UserDTO getSafeUser(String userName);

    public Instruction createMatchingInstruction(String instructionType, LocalDateTime time, boolean isBuy, String symbol, int price, int quantity, String userName) throws Exception;

    public int getQuantityOfStockByUser(String userName, String symbol) throws Exception;

    public boolean userExist(String userName);

    public int getUserTotalVal(String userName) throws Exception;

    public boolean loadXML(String xmlFileName) throws Exception;

    public void readStocksFromFile(String fileName) throws IOException, ClassNotFoundException;

    public void writeStocksToFile(String fileName) throws IOException;

    public void checkLegalQuantity(int quantity, String userName, String symbol, boolean isBuy) throws Exception;

    public void checkLegalPrice(int price) throws Exception;

    public void checkLegalInstructionType(String instructionType) throws Exception;

    public void checkLegalSymbol(String symbol, String userName) throws Exception;

    public void loadDataForTesting() throws Exception;

    public void prepNextAction() throws InterruptedException;

    public void cleanStocks();

    public void cleanUsers();

    public Map<String, User> getUsers();

}