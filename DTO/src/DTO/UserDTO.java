package DTO;

import SystemEngine.StocksTradeSystem;
import SystemEngine.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private List<StockPaperDTO> stocksOwnedByUser;

        public UserDTO(User user, StocksTradeSystem system) {
            stocksOwnedByUser = new ArrayList<>();
            for (String symbol :user.getStocksInBook().keySet()){
                try {
                    User.StockPaper stockPaper = user.getStocksInBook().get(symbol);
                    stocksOwnedByUser.add(new StockPaperDTO(system.getSafeStock(symbol).getPrice(),
                            stockPaper.getAtMarket(),stockPaper.getIdle(),
                            system.getSafeStock(symbol).getCompanyName(), symbol));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    public List<StockPaperDTO> getOwnedStocks() {
            return stocksOwnedByUser;
    }

    public class StockPaperDTO {
            private Integer currentPrice;
            private Integer idleQuantity;

        public void setIdleQuantity(Integer idleQuantity) { this.idleQuantity = idleQuantity; }
        public void setAtMarketQuantity(Integer atMarketQuantity) { this.atMarketQuantity = atMarketQuantity; }

        private Integer atMarketQuantity;
            private String companyName;
            private String symbol;

        public int getTotalAmount(){return currentPrice+idleQuantity; }

        public StockPaperDTO(Integer currentPrice, Integer atMarketQuantity, Integer idleQuantity, String companyName, String symbol) {
            this.currentPrice = currentPrice;
            this.atMarketQuantity = atMarketQuantity;
            this.idleQuantity = idleQuantity;
            this.companyName = companyName;
            this.symbol = symbol;
        }

        public Integer getIdleQuantity() {
                return idleQuantity;
            }
        public Integer getAtMarketQuantity() {
            return atMarketQuantity;
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
            this.idleQuantity = quantity;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }
    }
