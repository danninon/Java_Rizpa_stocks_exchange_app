package users;

import users.singleUserTab.SingleUserTabController;
import appControl.ApplicationControl;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class usersController {

    @FXML
    private TabPane tabPaneUsers;

    private List<SingleUserTabController> tabList;

    public void submitData() throws Exception {


        createUserTabs();

        }

    private void loadUserTabs() {

    }

    private void createUserTabs() throws Exception {
        appControl.createUserTabs(tabList, tabPaneUsers);

    }

    public usersController() {
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
