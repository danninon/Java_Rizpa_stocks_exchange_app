package SystemEngine;

import java.util.*;

public class User {
    private Map<String, StockPaper> stocksInBook = new HashMap<>();

    public static void transact(User operator, User buyer, User seller, Transaction tr, String symbol) {

    }

    public void addPaper(String symbol, int quanitity) throws Exception {
        stocksInBook.put(symbol, new StockPaper(0,quanitity));

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

    public void updateUserAfterBuying(String symbol, int quantityTraded) {
        StockPaper buyerStockPaper = stocksInBook.get(symbol);
        if (buyerStockPaper == null) {
            stocksInBook.put(symbol, new StockPaper(0, quantityTraded));
        }
        else {
            buyerStockPaper.idle += quantityTraded;
        }
    }

    public void updateUserAfterSelling(String symbol, int quantity){
        StockPaper sellerStockPaper = stocksInBook.get(symbol);
        sellerStockPaper.atMarket -= quantity;
       if (sellerStockPaper.atMarket + sellerStockPaper.idle == 0) {
           removePaper(symbol);
       }
    }

    public class StockPaper {
        int atMarket;
        int idle;

        public int getTotalAmount(){return atMarket + idle; }
        public StockPaper(int atMarket, int idle) {
            this.idle = idle; //total has
            this.atMarket = atMarket; //sold atm at market
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

        public int getAtMarket() {
            return atMarket;
        }

        public void setAtMarket(int atMarket) {
            this.atMarket = atMarket;
        }

        public int getIdle() {
            return idle;
        }

        public void setIdle(int idle) {
            this.idle = idle;
        }

        public void updateStock(int quantity) {

        }
    }
}


