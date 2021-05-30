package SystemEngine;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.TransactionDTO;
import SystemEngine.Instruction.Instruction;

import java.io.Serializable;
import java.rmi.activation.ActivateFailedException;
import java.util.*;

public class Stock implements Serializable {


    @Override
    public int hashCode() {
        return Objects.hash(SELL, BUY, NOT_FOUND, ZERO, BufferClearerCnt, companyName, stockPrice, BuyInstructions, SaleInstructions, pastTransactions);
    }



//    public CMD4ReturnBundle operate(Instruction newInstruction, Collection<Instruction> sameType, Collection<Instruction> oppositeType) throws ActivateFailedException {
//        CMD4ReturnBundle rb = new CMD4ReturnBundle();
//        for (Instruction oppositeInstrucion : oppositeType) {
//            if (!newInstruction.getOperatorName().equals(oppositeInstrucion.getOperatorName())) {
//                if (newInstruction.matchesOppositeInstruction(oppositeInstrucion)) {
//                    Transaction tr = oppositeInstrucion.operateStock(newInstruction);
//                    pastTransactions.push(tr);
//                    updateStock(tr.getQuantity(), tr.getPrice(), tr.getTotalPayment());
//               //     updateUsers(newInstruction, oppositeInstrucion, tr);
//                    rb.add(new TransactionDTO(tr));
//
//                    if (oppositeInstrucion.getQuantity() == 0){
//                        oppositeType.remove(oppositeInstrucion);
//                    }
//                    if (newInstruction.getQuantity() == 0) {
//                        break;
//                    }
//                }
//            }
//        }
//        boolean status = true;
//        if (newInstruction.getQuantity() > 0) {
//            newInstruction.prepareAddingRemainderToInstructionList(this);
//            status = sameType.add(newInstruction);
//             rb.setInsDTO(new InstructionDTO(newInstruction));
//        }
//        if (status == false){
//            throw new ActivateFailedException("Error loading to instruction list! This may be caused by various technical reasons. Call a technician");
//        }
//        return rb;
//    }

    private void updateUsers(Instruction newInstruction, Instruction oppositeInstruction, Transaction tr) {

    }


    public void updateStock(int quantity, int newPrice, int addToCycleSum){
    this.stockPrice = newPrice;
    this.fullCycle += addToCycleSum;
    }


    public void simplePrint() {
       // System.out.println("Symbol: " + symbol);
        System.out.println("Company: " + companyName);
        System.out.println("Price: " + stockPrice);
        System.out.println("Total transactions: " + pastTransactions.size());
        System.out.println("Full Cycle: " + fullCycle + "\n");
    }



    public void setPrice(int newPrice) {
        stockPrice = newPrice;
    }

    public Stock(String name, int price) throws Exception {
//        char[] chars = symbol.toCharArray();
//        for (char c: chars){
//            if (!Character.isLetter(c))
//                if (!Character.isUpperCase(c))
//                    throw new IllegalArgumentException("a stock's symbol must contain capital letters only." + ("Symbol in file: " + symbol));
//        }
//        this.symbol = symbol;

        if (price < 0) {
            throw new IllegalArgumentException("You've entered: " + price + "Price must be none-negative.");
        }
        this.stockPrice = price;

        char[] chars = name.toCharArray();
        for (char c: chars){
            if (!Character.isLetter(c) && (c != ' '))
                throw new IllegalArgumentException("a stock's company name must be consisted out of letters. it may also contain spaces." + ("Name in file: " + name));
        }
        this.companyName = name;
    }
    public int getNumOfTransitions(){return pastTransactions.size();}



    public int getPrice() {
        return stockPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCycle() {
        return fullCycle;
    }

    public LinkedList<Transaction> getTransactionList() {
        return pastTransactions;
    }

    public Collection<Instruction> getSaleInstructionData() {
        return SaleInstructions;
    }

    public Collection<Instruction> getBuyInstructionData() {
        return BuyInstructions;
    }


    final int SELL = 1;
    final int BUY = 0;
    final int NOT_FOUND = -1;
    final int ZERO = 0;

    int BufferClearerCnt = 0;

    //private final String symbol;
    private final String companyName;
    private int stockPrice = 0, fullCycle = 0;


    private void closeFinishedInstructions(){
        Iterator<Instruction> itr1 = BuyInstructions.iterator();
        while(itr1.hasNext()) {
            if (itr1.next().getQuantity() == 0)
                itr1.remove();
        }
        Iterator<Instruction> itr2 = SaleInstructions.iterator();
        while(itr2.hasNext()) {
        if (itr2.next().getQuantity() == 0)
                itr2.remove();
        }
    }

    public void clearCache(){
        closeFinishedInstructions();
    }

    private Set<Instruction> BuyInstructions = new TreeSet<>();
    private Set<Instruction> SaleInstructions = new TreeSet<>();
    private LinkedList<Transaction> pastTransactions = new LinkedList<>();


}
