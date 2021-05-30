package usersTabPane;

import usersTabPane.singleUserTab.SingleUserTabController;
import appControl.ApplicationControl;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.List;

public class UsersController {

    @FXML
    private TabPane tabPaneUsers;

    private List<SingleUserTabController> tabList;

    private void loadUserTabs() {

    }

    public UsersController() {
        tabList = new ArrayList<SingleUserTabController>();
        System.out.println("OutputUsersController constructor");
    }

    public void initialize() {
        System.out.println("OutputUsersController init");

    }

//    public void requestUpdateUsers() {
//
//    }



    public void setMainController(ApplicationControl applicationController) {
        appControl = applicationController;
    }


    private ApplicationControl appControl;



}
