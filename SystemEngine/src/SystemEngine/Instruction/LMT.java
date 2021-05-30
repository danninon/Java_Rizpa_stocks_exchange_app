package SystemEngine.Instruction;

import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public class LMT extends Instruction {

    public LMT(LocalDateTime time, boolean isBuy, int price, int quantity, String operatorName) throws Exception {
        super(time, isBuy, price, quantity, operatorName);
    }

    @Override
    public void prepareAddingRemainderToInstructionList(Stock addedTo) {

    }

    @Override
    public void buyStocks() {

    }

    @Override
    public void sellStocks() {

    }

    @Override
    public Transaction operateStock(Instruction newInstruction) {
        int legalQuantity = min(quantity, newInstruction.getQuantity());
        this.reduceQuantity(legalQuantity);
        newInstruction.reduceQuantity(legalQuantity);
        return new Transaction(newInstruction.time, price, legalQuantity, newInstruction.getClass().getSimpleName(), newInstruction.getOperatorName(), newInstruction.isBuy(), this.getOperatorName());
    }


    @Override
    public boolean matchesOppositeInstruction(Instruction newInstruction) {
        if (newInstruction.isBuy) {
            if (this.price <= ((LMT) newInstruction).price)
                return true;
            else
                return false;
        } else {
            if (this.price >= ((LMT) newInstruction).price)
                return true;
            else
                return false;
        }
    }


}

