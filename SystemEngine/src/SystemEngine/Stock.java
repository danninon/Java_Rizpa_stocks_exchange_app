package SystemEngine;

import SystemEngine.Instruction.Instruction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Stock implements Serializable {


    @Override
    public int hashCode() {
        return Objects.hash(SELL, BUY, NOT_FOUND, ZERO, BufferClearerCnt, companyName, stockPrice, BuyInstructions, SaleInstructions, pastTransactions);
    }



    public void updateStock(int quantity, int newPrice, int addToCycleSum){
    this.stockPrice = newPrice;
    this.fullCycle += addToCycleSum;
    }

    public void setPrice(int newPrice) {
        stockPrice = newPrice;
    }

    public void simplePrint() {
        // System.out.println("Symbol: " + symbol);
        System.out.println("Company: " + companyName);
        System.out.println("Price: " + stockPrice);
        System.out.println("Total transactions: " + pastTransactions.size());
        System.out.println("Full Cycle: " + fullCycle + "\n");
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

    public Collection<Instruction> getSaleInstructionData() { return SaleInstructions;    }

    public Collection<Instruction> getBuyInstructionData() {
        return BuyInstructions;
    }


    private void closeFinishedInstructions(Stock stock){

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

    final int SELL = 1;
    final int BUY = 0;
    final int NOT_FOUND = -1;
    final int ZERO = 0;

    int BufferClearerCnt = 0;


    private final String companyName;
    private int stockPrice = 0, fullCycle = 0;


    private Set<Instruction> BuyInstructions = new TreeSet<>();
    private Set<Instruction> SaleInstructions = new TreeSet<>();
    private LinkedList<Transaction> pastTransactions = new LinkedList<>();
    private List<TimePriceVector> priceTimeData = new ArrayList();



    public void savePriceTimeVector(int price, LocalDateTime time) {
        priceTimeData.add(new TimePriceVector(price, time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"))));
    }

    public List<TimePriceVector> getPriceTimeVectorList() {
        return priceTimeData;
    }


    public class TimePriceVector{
        private String time;
        private Integer price;

        public TimePriceVector(Integer price,String time) {
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
