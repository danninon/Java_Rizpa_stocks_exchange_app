package SystemEngine.Instruction;

import SystemEngine.Stock;
import SystemEngine.Transaction;

import java.time.LocalDateTime;

public class MKT extends LMT {
    public MKT(LocalDateTime _time, boolean _isBuy, int _quantity, String operatorName) throws Exception {
        super(_time, _isBuy, 0, _quantity, operatorName);
    }

    //return status?
    public void prepareAddingRemainderToInstructionList(Stock addedTo) {
        boolean status;
  //      price = addedTo.getPrice();
//        if (isBuy)
//            status = addedTo.getBuyInstructionData().add(this);
//        else
//            status = addedTo.getSaleInstructionData().add(this);
    }

    @Override
    public void buyStocks() {

    }

    @Override
    public void sellStocks() {

    }

    @Override
    public Transaction operateStock(Instruction newInstruction) {
        newInstruction.setPrice(this.price);
        return super.operateStock(newInstruction);
    }

    @Override
    public boolean matchesOppositeInstruction(Instruction newBuyInstruction) {
        return true;
    }

}


