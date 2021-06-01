package DTO;

import SystemEngine.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public TransactionDTO(Transaction originalTransaction) {
        this.isNew = originalTransaction.getIfNew();
        this.time = originalTransaction.getTime();
        this.strTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));

        this.totalPayment = originalTransaction.getQuantity()*originalTransaction.getQuantity();
        this.price = originalTransaction.getPrice();
        this.quantity = originalTransaction.getQuantity();

        this.instructionType = originalTransaction.getInstructionType();
        this.invokersName = originalTransaction.getInvokedName();
        this.buyerName = originalTransaction.getBuyersName().substring(0,1).toUpperCase() + originalTransaction.getBuyersName().substring(1);
        this.sellerName = originalTransaction.getSellersName().substring(0,1).toUpperCase() + originalTransaction.getSellersName().substring(1);

    }

    public boolean isNew() {
        return isNew;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPayment() {
        return price*quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getInstructionType() {return instructionType;}

    public String getStrTime() {return strTime;}

    private boolean isNew = true;
    private final LocalDateTime time;
    private final String strTime;

    private final int price;
    private final int quantity;
    private final int totalPayment;

    private final String instructionType;
    private String invokersName;
    private String buyerName, sellerName;

    public String getInvokersName() {
        return invokersName;
    }


}
