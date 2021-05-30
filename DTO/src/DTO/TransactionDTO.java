package DTO;

import SystemEngine.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {
    public String getBuyerName() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSellerName() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public TransactionDTO(Transaction originalTransaction) {
        this.isNew = originalTransaction.getIfNew();
        this.time = originalTransaction.getTime();
        this.strTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        this.price = originalTransaction.getPrice();
        this.quantity = originalTransaction.getQuantity();
        this.instructionType = originalTransaction.getInstructionType();
        this.invokersName = originalTransaction.getInvokedName();
        this.buyer = originalTransaction.getBuyersName();
        this.seller = originalTransaction.getSellersName();

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
    private final String instructionType;
    String invokersName;
    String buyer, seller;
    public String getInvokersName() {
        return invokersName;
    }


}
