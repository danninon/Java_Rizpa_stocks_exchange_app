package DTO;

import SystemEngine.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {
    public TransactionDTO(Transaction originalTransaction) {
        this.isNew = originalTransaction.getIfNew();
        this.time = originalTransaction.getTime();
        this.strTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        this.price = originalTransaction.getPrice();
        this.quantity = originalTransaction.getQuantity();
        this.instructionType = originalTransaction.getInstructionType();
        this.invokedName = originalTransaction.getInvokedName();
        this.buyer = originalTransaction.getBuyer();
        this.seller = originalTransaction.getSeller();

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
    String invokedName;
    String buyer, seller;
    public String getInvokedName() {
        return invokedName;
    }


}
