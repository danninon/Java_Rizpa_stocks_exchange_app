package DTO;

import SystemEngine.Instruction.Instruction;
import SystemEngine.Transaction;

import java.util.LinkedList;
import java.util.List;

public class CMD4ReturnBundle {
    private LinkedList<TransactionDTO> transactionList = new LinkedList<>();
    private InstructionDTO newInstruction;

    public CMD4ReturnBundle(LinkedList<Transaction> transactionList, Instruction newInstruction) {
        if (transactionList != null) {
            if (transactionList.size() != 0) {
                transactionList.forEach((tr) -> this.transactionList.add(new TransactionDTO(tr)));
            }
        }
        if (newInstruction.getQuantity() != 0) {
            this.newInstruction = new InstructionDTO(newInstruction);
        }
    }

    public void setInsDTO(InstructionDTO instruction) {
        newInstruction = instruction;
    }

    public InstructionDTO getInsDTO() {
        return newInstruction;
    }

    public List<TransactionDTO> getTransactionsMade() {
        return transactionList;
    }


    public void add(TransactionDTO completeOperations) {
        this.transactionList.add(completeOperations);
    }

    public void setIns(InstructionDTO ins) {
        this.newInstruction = ins;
    }
}

