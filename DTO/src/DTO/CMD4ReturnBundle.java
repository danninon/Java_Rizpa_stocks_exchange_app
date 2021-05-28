package DTO;

import java.util.LinkedList;
import java.util.List;

public class CMD4ReturnBundle {
    private List<TransactionDTO> transactionsMade = new LinkedList<>();
    private InstructionDTO ins = null;

    public void setInsDTO(InstructionDTO instruction) {
        ins = instruction;
    }

    public InstructionDTO getInsDTO() {
        return ins;
    }

    public List<TransactionDTO> getTransactionsMade() {
        return transactionsMade;
    }


    public void add(TransactionDTO completeOperations) {
        this.transactionsMade.add(completeOperations);
    }

    public void setIns(InstructionDTO ins) {
        this.ins = ins;
    }
}

