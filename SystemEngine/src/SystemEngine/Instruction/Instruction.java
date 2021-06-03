package SystemEngine.Instruction;

import SystemEngine.Exceptions.NullPriceException;
import SystemEngine.Exceptions.NullQuantityException;
import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public abstract class Instruction implements Serializable, Comparable {

    abstract public void setPriceAfterNoTransaction(int newPrice);

    public void setQuantity(int _quantity) {
        quantity = _quantity;
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getCycle() {
        return price*quantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isBuy(){return isBuy;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return quantity == that.quantity && price == that.price &&  isBuy == that.isBuy && isNew == that.isNew && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, time, price, isBuy, isNew);
    }


    @Override
    public String toString() {

//        return "SystemEngine.Instruction.Instruction{" +
//                "quantity=" + quantity +
//                ", date='" + date + '\'' +
//                ", price=" + price +
//                ", cycle=" + cycle +
//                ", instructionComplete=" + instructionComplete +
//                ", relationsWithInvoker=" + relationsWithInvoker +
//                '}';
        return "| " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "| " + price + "| " + quantity + "| ";// + price*quantity + "|";
    }

    protected int quantity;
    protected LocalDateTime time;
    protected int price = 0;
    protected boolean isBuy;
    protected boolean isNew = true;
    protected String operatorName;

    public abstract Transaction operateStock(Instruction newBuyInstruction);

    public abstract boolean matchesOppositeInstruction(Instruction newBuyInstruction);

    public abstract void prepareAddingRemainderToInstructionList(Stock addedTo);

    public boolean checkIfNew (){return isNew;}

    public void setIsNew(boolean b) {
        isNew = b;
    }

    public String getOperatorName(){return operatorName; }
    Instruction(LocalDateTime time, boolean isBuy, int price, int quantity, String operatorName) throws Exception {
        this.time = time;
        this.isBuy = isBuy;
        this.price = price;
        this.quantity = quantity;
        this.operatorName = operatorName;
        this.checkLegalInstruction();

    }

    private void checkLegalInstruction() throws Exception {
        if (this.getClass() != MKT.class) {
            if (price == 0) throw new NullPriceException("Illegal operation:\nThe price cannot be zero.");
        }
            if (quantity == 0) throw new NullQuantityException("You cannot operateStock with the quantity of 0. u nuts?");

            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative!");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative!");
            }
    }

    public String getInvokersName(){return operatorName;}

    protected void setPrice(int price) {
        this.price = price;
    }
    final int NOT_INIT = 0;



    @Override
    public int compareTo(Object o1) {
//
        LMT otherIns = ((LMT) o1);
        int priceSub = this.price - otherIns.price;
        if (price == otherIns.price && !this.equals(otherIns)){
            int res = time.compareTo(otherIns.time);
            return res;
        } else if (this.isBuy == true)
            return -priceSub;
        else
            return priceSub;


    }


}
