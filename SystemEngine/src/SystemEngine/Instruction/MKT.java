package SystemEngine.Instruction;

import SystemEngine.Stock;
import SystemEngine.StockTradingSystem;
import SystemEngine.Transaction;

import java.time.LocalDateTime;

public class MKT extends LMT {
    public MKT(LocalDateTime _time, boolean _isBuy, int _quantity, String operatorName) throws Exception {
        super(_time, _isBuy, 0, _quantity, operatorName);
    }


    //return status?
    @Override
    public void prepareAddingRemainderToInstructionList(Stock stock) {
        boolean status;
        this.price = stock.getPrice();
    }

    @Override
    public void setPriceAfterNoTransaction(int newPrice) {
        this.price =  newPrice;
    }


    @Override
    public Transaction operateStock(Instruction oppositeInstruction) {
       this.price = oppositeInstruction.price;
        return super.operateStock(oppositeInstruction);
    }

    @Override
    public boolean matchesOppositeInstruction(Instruction newBuyInstruction) {
        return true;
    }

}


