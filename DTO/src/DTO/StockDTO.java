package DTO;

import SystemEngine.Instruction.Instruction;
import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.util.*;

public class StockDTO {
    public StockDTO(Stock stock) {

        this.companyName = stock.getCompanyName();
        this.price = stock.getPrice();
        this.fullCycle = stock.getCycle();
        for (Instruction originalInstruction: stock.getSaleInstructionList()){
            saleListDTO.add(new InstructionDTO(originalInstruction)) ;
        }
        for (Instruction originalInstruction: stock.getBuyInstructionList()){
            buyListDTO.add(new InstructionDTO(originalInstruction)) ;
        }
        for (Transaction originalTransaction: stock.getTransactionList()){
            transactionListDTO.push(new TransactionDTO(originalTransaction));
        }

    }



    public String getCompanyName() {
        return companyName;
    }

    public int getPrice() {
        return price;
    }

    public int getCycle(){
        return fullCycle;
    }

    public List<InstructionDTO> getSaleList() {
        return saleListDTO;
    }

    public List<InstructionDTO> getBuyList() {
        return buyListDTO;
        }
    public List<TransactionDTO> getTransactionList(){
        return transactionListDTO;
    }




    private String companyName;
    private int price, fullCycle;
    private List<InstructionDTO> saleListDTO = new ArrayList<>();
    private List<InstructionDTO> buyListDTO  = new ArrayList<>();
    private LinkedList<TransactionDTO> transactionListDTO  = new LinkedList<>();


}
