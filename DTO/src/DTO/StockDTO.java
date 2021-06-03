package DTO;

import SystemEngine.Instruction.Instruction;
import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.time.LocalDateTime;
import java.util.*;

public class StockDTO {
    public StockDTO(Stock stock) {

        this.companyName = stock.getCompanyName();
        this.price = stock.getPrice();
        this.fullCycle = stock.getCycle();
        for (Instruction originalInstruction: stock.getSaleInstructionData()){
            saleListDTO.add(new InstructionDTO(originalInstruction)) ;
        }
        for (Instruction originalInstruction: stock.getBuyInstructionData()){
            buyListDTO.add(new InstructionDTO(originalInstruction)) ;
        }
        for (Transaction originalTransaction: stock.getTransactionList()){
            transactionListDTO.push(new TransactionDTO(originalTransaction));
        }
        for (Stock.TimePriceVector vector: stock.getPriceTimeVectorList()){
            priceTimeDataListDTO.add(new TimePriceVectorDTO(vector.getPrice(), vector.getTime()));
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

    private List<TimePriceVectorDTO> priceTimeDataListDTO = new ArrayList();



    public List<TimePriceVectorDTO> getPriceTimeDataList() {
      return priceTimeDataListDTO;
    }


    public class TimePriceVectorDTO{
        private String time;
        private Integer price;

        public TimePriceVectorDTO(Integer price,String time) {
            this.time = time;
            this.price = price;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }
    }

}
