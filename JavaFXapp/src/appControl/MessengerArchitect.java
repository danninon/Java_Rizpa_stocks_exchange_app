package appControl;

import DTO.CMD4ReturnBundle;
import DTO.InstructionDTO;
import DTO.TransactionDTO;
import javafx.beans.property.StringProperty;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessengerArchitect {
    String matchingActionMSG(CMD4ReturnBundle bundle, boolean buySelected) {
        //  boolean isBuy = isCurrentlyBuying();
        String retMSG = "";
        if (bundle.getInsDTO() == null) { //instruction wasn't made
            if (buySelected) {
                retMSG += "Perfect match found!\nSuccessfully acquired the full extent of the request. \n ";
            } else {
                retMSG += "Perfect match found!\nSuccessfully sold the full extent of the request. \n ";
            }
            retMSG += concatTransactions(bundle.getTransactionsMade());
        }//theres an instruction
        //case no transactions were made - added full instruction
        else if (bundle.getTransactionsMade() == null)  //transactions were made
        {
            if (buySelected) {
                retMSG += "There are no active sale instruction that matches with your request.\n";
                retMSG += "The full  instruction has been added to the market. \n";
            } else {
                retMSG += "The full sale instruction that has been added to the market. \n";
                retMSG += "There are no active instruction that matches with your request.\n";
            }
            retMSG += instructionTimePriceQuantity(bundle.getInsDTO()); //Do: doesn't print

        }
        //theres and instruction and at least some transactions
        else { //new instruction and no transaction - no transaction has been made
            if (buySelected) {
                retMSG += "Partially match found!\nSuccessfully bought some of the request.\n";
                retMSG += "\nThis partial updateBuyer instruction was been added to the market(the reminder after partially buying some of the stocks): \n";
            } else {
                retMSG += "Partially match found!\nSuccessfully sold some of the request.\n";
                retMSG += "\nThis partial sale instruction was added to the market(the reminder after partially selling some of the stock): \n";
            }
            retMSG += concatTransactions(bundle.getTransactionsMade());
            retMSG += instructionTimePriceQuantity(bundle.getInsDTO()); //Do: doesn't print
        }
        return retMSG;
    }

     public String instructionTimePriceQuantity(InstructionDTO ins) {
        return "Time - " + ins.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\nPrice - " + ins.getPrice() +
                " NIS\nQuantity - " + ins.getQuantity() + "\nFor a total price of:" + ins.getQuantity() * ins.getPrice() + " NIS";
    }

    public  String concatTransactions(List<TransactionDTO> transactionDTO) {
        String addedRetMSG = "";
        int index = 1;
        System.out.println("Updated Transaction List - ");
        if (transactionDTO.isEmpty()) {
            System.out.println("There are no transactions in our archives.");
        } else {
            for (TransactionDTO tr : transactionDTO) {
                addedRetMSG += stockHeaderFormat(index++) + presentingTransaction(tr);
            }
        }
        return addedRetMSG;
    }

    public  String presentingTransaction(TransactionDTO tr) {
        return "Time - " + tr.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS")) + "\nPrice - " + tr.getPrice() +
                "\nQuantity - " + tr.getQuantity() + "\nCycle - " + tr.getTotalPayment();
    }

    public  String stockHeaderFormat(int i) {
        return "|~~~~~~~~~~~~~~~~~~~~" + (i) + "~~~~~~~~~~~~~~~~~~~~|\n";
    }

     public void updateTextProperty(StringProperty textUserInformationProperty, Exception e) {
        textUserInformationProperty.setValue(e.getMessage());
    }

    public String openingMessage() {
        return "Salutations!\n" +  "You've chosen to use RizpaMarketManager V2.15\n"
                + "To begin, please load an XML data file by pressing the corresponding button at the top right.";
    }


}
