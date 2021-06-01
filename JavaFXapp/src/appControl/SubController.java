package appControl;

import SystemEngine.StocksTradeSystem;

public interface SubController {
    public void setManager(StocksTradeSystem manager);

    public void setMainController(ApplicationControl applicationController);

    public void submitData();
    //public void knowAudience(SubScreen screen);

    // public void informConnections();

    public enum Type {
        STOCKS_BOOK, TRADE, INTRODUCTION, TOP
    }

    }

