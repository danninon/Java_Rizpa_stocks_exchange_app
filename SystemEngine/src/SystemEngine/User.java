package SystemEngine;

import java.util.*;

public class User {
    private Map<String, Integer> stocksInBook = new HashMap<>();

    public void addPaper(String symbol, int quanitity) throws Exception {
        stocksInBook.put(symbol, quanitity);

    }

    public Map<String, Integer> getStocksInBook() {
        return stocksInBook;
    }

    //replacces the vlaues
    public void setQuantity(String symbol,int newVal){ ;
        stocksInBook.put(symbol, newVal);
    }
    public Integer getQuantity(String symbol) throws Exception {
        return stocksInBook.get(symbol);
    }

    public boolean symbolExistsInBook(String symbol){
            if (stocksInBook.containsKey(symbol))
                return true;
            else
                return false;
    }



}


