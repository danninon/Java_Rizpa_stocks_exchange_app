package DTO;

import SystemEngine.User;

import java.util.HashMap;
import java.util.Map;

public class StocksBookDTO {
    public StocksBookDTO(User book){
        dtobook = book.getStocksInBook();
    }

    Map<String,Integer> dtobook = new HashMap<String, Integer>();

    public Map<String, Integer> getDtobook() {
        return dtobook;
    }

    public void setDtobook(Map<String, Integer> dtobook) {
        this.dtobook = dtobook;
    }
}
