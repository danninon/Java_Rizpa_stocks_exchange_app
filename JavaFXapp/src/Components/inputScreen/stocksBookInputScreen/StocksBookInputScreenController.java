package Components.inputScreen.stocksBookInputScreen;

import SystemEngine.MarketManager;
import SystemEngine.StocksTradeSystem;
import appControl.ApplicationControl;
import appControl.SubController;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StocksBookInputScreenController implements SubController{

    @FXML private AnchorPane stocksBookController;
    @FXML private TextArea DebugTextSpace;
    @FXML private ChoiceBox<String> comboBoxLeftChooseStock;
    @FXML private Button buttonSearchStock;

    @Override public void setManager(StocksTradeSystem marketManager) {
        this.marketManager = marketManager;
    }
    @Override public void setMainController(ApplicationControl applicationController)
    {
        this.appControl = applicationController;
    }
    @Override public void submitData() {
        comboBoxLeftChooseStock.getItems().addAll(marketManager.getSafeStocks().keySet());
    }

    @FXML
    void loadStockPageRequested(ActionEvent event) throws Exception {
//        appControl.requestUpdateStocksBook(comboBoxLeftChooseStock.getSelectionModel().getSelectedItem());
    }

    @FXML
    void requestLoadBook(MouseEvent event) {

    }
    private StocksTradeSystem marketManager;
    private ApplicationControl appControl;
}
