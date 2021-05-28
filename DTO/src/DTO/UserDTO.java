package DTO;

import SystemEngine.StocksTradeSystem;
import SystemEngine.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

        public UserDTO(User user, StocksTradeSystem system) {
            stocksOwnedByUser = new ArrayList<>();
            for (String symbol :user.getStocksInBook().keySet()){
                try {
                    stocksOwnedByUser.add(new StockPaperDTO(system.getSafeStock(symbol).
                            getPrice(),user.getQuantity(symbol),system.getSafeStock(symbol).getCompanyName(), symbol));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<StockPaperDTO> stocksOwnedByUser;

    public List<StockPaperDTO> getOwnedStocks() {
            return stocksOwnedByUser;
    }

    public class StockPaperDTO {
            private Integer currentPrice;
            private Integer quantity;
            private String companyName;
            private String symbol;

        public StockPaperDTO(Integer currentPrice, Integer quantity, String companyName, String symbol) {
            this.currentPrice = currentPrice;
            this.quantity = quantity;
            this.companyName = companyName;
            this.symbol = symbol;
        }

        public Integer getQuantity() {
                return quantity;
            }

            public String getSymbol() {
                return symbol;
            }

        public Integer getCurrentPrice() {
            return currentPrice;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCurrentPrice(Integer currentPrice) {
            this.currentPrice = currentPrice;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }
    }
