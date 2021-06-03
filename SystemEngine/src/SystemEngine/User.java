package SystemEngine;

import java.util.*;

public class User {
    private Map<String, StockPaper> stocksInBook = new HashMap<>();

    public static void transact(User operator, User buyer, User seller, Transaction tr, String symbol) {

    }

    public void addPaper(String symbol, int idleQuantity) throws Exception {
        stocksInBook.put(symbol, new StockPaper(0,idleQuantity));

    }

    public Map<String, StockPaper> getStocksInBook() {
        return stocksInBook;
    }


    public boolean symbolExistsInBook(String symbol){
            if (stocksInBook.containsKey(symbol))
                return true;
            else
                return false;
    }


    public void removePaper(String symbol) {
        stocksInBook.remove(symbol);
    }

    public StockPaper getPaper(String symbol) {
        return stocksInBook.get(symbol);
    }

    public void updateUserNewInstruction(String symbol, int buyingQuantity){
        stocksInBook.get(symbol).atMarket += buyingQuantity;
    }

    public void updateBuyer(String symbol, int incQuantity) {
        StockPaper buyerStockPaper = stocksInBook.get(symbol);
        if (buyerStockPaper == null) {
            stocksInBook.put(symbol, new StockPaper(0, incQuantity));
        }
        buyerStockPaper.idle += incQuantity;
    }
//if true remove from idle else remove from in market
    public void updateSeller(boolean sellerIsInitiator, String symbol, int deduceQuantity){

        StockPaper sellerStockPaper = stocksInBook.get(symbol);
        if (sellerIsInitiator){
            sellerStockPaper.idle -= deduceQuantity;
        }
        else{
            sellerStockPaper.atMarket -= deduceQuantity;
        }
        if (sellerStockPaper.atMarket + sellerStockPaper.idle == 0) {
            removePaper(symbol);
        }
    }

    public void updateUserAfterInsertingSaleInstruction(String symbol, Integer incValueToAtMarket){
        stocksInBook.get(symbol).setAtMarket(stocksInBook.get(symbol).getAtMarket() + incValueToAtMarket);
        stocksInBook.get(symbol).setIdle(stocksInBook.get(symbol).getIdle() - incValueToAtMarket) ;
    }

    public class StockPaper {
       private Integer atMarket = 0;
        private Integer idle = 0;

        public int getTotalAmount(){return atMarket + idle; }
        public StockPaper(int atMarket, int idle) {
            this.idle = idle; //total has
            this.atMarket = +atMarket; //sold atm at market
            //this means inbag equals inpossetion - at market
        }

        public void updateThisAfterBought(int quantityTraded) {
            idle += quantityTraded;
        }

        public void updateAfterSold(int quantityTraded, String symbol) {
            atMarket -= quantityTraded;
            if (idle+atMarket == 0 ){
                removePaper(symbol);
            }
        }

        public Integer getAtMarket() {
            return atMarket;
        }

        public void setAtMarket(int atMarket) {
            this.atMarket = atMarket;
        }

        public Integer getIdle() {
            return idle;
        }

        public void setIdle(int idle) {
            this.idle = idle;
        }

        public void updateStock(int quantity) {

        }
    }
}


