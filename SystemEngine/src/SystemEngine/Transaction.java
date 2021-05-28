package SystemEngine;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {

    public LocalDateTime getTime() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    } //should return sharesRate

    public int getTotalPayment() {
        return price*quantity;
    }

    public String getInstructionType(){return instructionType; }

    public String getInvokedName(){return invokedName;}

    public Transaction(LocalDateTime time, int price, int quantity, String instructionType, String invokedName, Boolean isBuying, String operatorName) {
        this.date = time;
        this.price = price;
        this.quantity = quantity;
        this.instructionType = instructionType;
        this.invokedName = invokedName;
        if (isBuying) {
            buyer = invokedName;
            seller = operatorName;
        }
        else {
            seller = invokedName;
            buyer = operatorName;
        }
    }

    @Override
    public String toString() {
        return "| " + date.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "| " + price + "| " + quantity + "| " + price*quantity + "|";

    }
//    protected  Instruction Instruction;
    protected boolean isNew = true;
    protected LocalDateTime date;
    protected int price;
    protected int quantity;
    String invokersName, invokedName;
    String buyer, seller;
    public boolean getIfNew(){return isNew;}
    public void setIsNew(boolean b){isNew = b;}
    private String instructionType;

    public String getBuyer() { return buyer; }

    public String getSeller() { return seller; }
}
