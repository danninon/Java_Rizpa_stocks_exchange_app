package DTO;

import SystemEngine.Instruction.Instruction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InstructionDTO implements Comparable {
    public InstructionDTO(Instruction originalIns) {
        this.quantity = originalIns.getQuantity();
        this.time = originalIns.getDate();
        this.price = originalIns.getPrice();
        this.isNew = originalIns.checkIfNew();
        this.isBuy = originalIns.isBuy();
        this.instructionType = originalIns.getClass().getSimpleName();
        this.operatorName = originalIns.getInvokersName();
        this.strTime = originalIns.getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
    }
    public InstructionDTO(){}

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getInstructionType(){return instructionType; }

    public boolean getIsBuy() {return isBuy;}

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public int getCycle() {
        return price*quantity;
    }

//    public String getInstructionType(){
//        return instructionType;
//    }
//    public String getOperatorsName() {
//        return operatorName;
//    }

    public boolean isNew() {
        return isNew;
    }
    private int quantity;
    private LocalDateTime time;
    private int price = 0;
    private boolean isBuy, isNew = true;
    private String instructionType;

    private String operatorName;
    private String strTime;

    public int compareTo(Object o1) {
        InstructionDTO otherIns = ((InstructionDTO) o1);
        int priceSub = this.price - otherIns.price;

        if (this.isBuy == true) {
            if (this.price == otherIns.price) {
//                System.out.println(this.date.getNano() - otherIns.date.getNano());
//                return this.date.getNano() - otherIns.date.getNano();
                return -this.time.compareTo(otherIns.time);
            }
            else {
                return -priceSub;
            }
        } else {
            if (this.price == otherIns.price) {
                return -this.time.compareTo(otherIns.time);
            }
            else {
                return priceSub;
            }

        }
    }


    public boolean isBuy() {
        return isBuy;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getStrTime() {
        return strTime;
    }
}
