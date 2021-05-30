package DTO;


import java.util.*;

public class MarketDTO {
    private Map<String, StockDTO> safeStocks;
    private Map<String, UserDTO> userDB;

    public MarketDTO(Map stocks) {
        safeStocks = new HashMap<>();
        safeStocks.putAll(stocks);
    }

    public Map<String, StockDTO> getSafeStocks() {
        return safeStocks;
    }
}


