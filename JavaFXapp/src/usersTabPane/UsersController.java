package usersTabPane;

import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import usersTabPane.adminTab.AdminTabController;
import usersTabPane.singleUserTab.SingleUserTabController;
import appControl.ApplicationControl;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import java.util.List;

public class UsersController {

    @FXML
    private TabPane mainTabPane;



    private Tab previouslySelectedUserTab;

    private ApplicationControl appControl;
    private Tab tabAdmin;
    private AdminTabController adminTabController;

    private List<SingleUserTabController> userTabsControllers;

    public void setUserByIndex(int index) {
        Tab tab = new Tab();
        mainTabPane.setSelectionModel(previouslySelectedUserTab.getTabPane().getSelectionModel());
    }

    public void setPreviouslySelectedUserTab(Tab previouslySelectedUserTab) {
        this.previouslySelectedUserTab = previouslySelectedUserTab;
    }

    public UsersController() {
        System.out.println("OutputUsersController constructor");
      //  previouslySelectedUserIndex = new SimpleIntegerProperty();
    }

    public void initialize() {
        System.out.println("OutputUsersController init");

    }

//    public void setTabSelectionProperty(Tab lastSelectedUserName){
//        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
//            newValue = lastSelectedUserName;
//        });
//
//
//        }


    public void setMainController(ApplicationControl applicationController) {
        appControl = applicationController;
    }

    public void getAdminOperatedStock() {

    }

    public void setAdminTab(Tab tabAdmin) {
        this.tabAdmin = tabAdmin;
    }

    public void setAdminController(AdminTabController adminTabController) {
        this.adminTabController = adminTabController;

    }

//    private void addToTabList(List<SingleUserTabController> tabControllersList){
//        this.tabList = tabControllersList;
//    }

    public String getOpenStockAtAdmin() {
        return adminTabController.getOpenStock();
    }

//    public void setAdminSelectedStock(String openStock) {
//        adminTabController.setAdminSelectedStock(openStock);
//    }

    public AdminTabController getAdminController() {
        return adminTabController;
    }

    public SingleSelectionModel<Tab> getOpenTabModel() {
        return mainTabPane.getSelectionModel();
    }


    public String getOpenTab() {
        SelectionModel<Tab>  tab = mainTabPane.getSelectionModel();
        Tab selectedItem = tab.getSelectedItem();
        String retName = null;
        if (selectedItem != null) {
            retName = selectedItem.getText();
        }
        return retName;
    }

    public void setTabControllerList(List<SingleUserTabController> userTabs) {
        this.userTabsControllers = userTabs;
    }


    public List<SingleUserTabController> getCurrentSingleUserController() {
        return userTabsControllers;
    }

    public SingleUserTabController getCurrentUserController(String currentlySelectedTabName) {
        SingleUserTabController retController = null;
        for (SingleUserTabController control: userTabsControllers){
            if (control.getUserName().equals(currentlySelectedTabName)){
                retController = control;
                break;
            }
        }
        return retController;
    }

    public Tab getAdminTab() {
        return tabAdmin;
    }
}
