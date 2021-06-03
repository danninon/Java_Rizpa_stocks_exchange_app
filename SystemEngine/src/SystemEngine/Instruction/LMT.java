package SystemEngine.Instruction;

import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public class LMT extends Instruction {

    private Stock addedTo;

    public LMT(LocalDateTime time, boolean isBuy, int price, int quantity, String operatorName) throws Exception {
        super(time, isBuy, price, quantity, operatorName);
    }

    @Override
    public void prepareAddingRemainderToInstructionList(Stock addedTo) {

    }



    @Override
    public void setPriceAfterNoTransaction(int searchedStock) {

    }

    @Override
    public Transaction operateStock(Instruction oppositeInstruction) {
        int legalQuantity = min(quantity, oppositeInstruction.getQuantity());
        this.reduceQuantity(legalQuantity);
        oppositeInstruction.reduceQuantity(legalQuantity);
        return new Transaction(oppositeInstruction.time, oppositeInstruction.getPrice(), legalQuantity, oppositeInstruction.getClass().getSimpleName(), oppositeInstruction.getOperatorName(), oppositeInstruction.isBuy(), this.getOperatorName());
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

