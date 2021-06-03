package appControl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminLog {

    private List<AdminAction> adminActionList;


    public void add(int actionNum, boolean status, String MSG, LocalDateTime time){
        adminActionList.add(new AdminAction(actionNum, status, MSG, time));
    }
    public AdminLog(ApplicationControl applicationControl) {
        adminActionList = new ArrayList<>();
        this.applicationControl = applicationControl;
    }

    public List<AdminAction> getMyEventList() {
        return adminActionList;
    }

    public class AdminAction {
        public AdminAction(int actionNum, boolean status, String details, LocalDateTime time) {
            this.actionNum = actionNum;
            this.status = status;
            this.details = details;
            this.time = time;
            this.strTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
        }

        private final int actionNum;
        private final boolean status;
        private final String details;
        private final String strTime;
        private final LocalDateTime time;


        public boolean isStatus() {
            return status;
        }
        public String getDetails() {
            return details;
        }
        public String getStrTime() {
            return strTime;
        }
        public LocalDateTime getTime() {
            return time;
        }
        public int getActionNum() {
            return actionNum;
        }
    }

    public final String BOOT = "System Boot";
    public final String LOAD_XML = "Load XML file";
    public final String TRADE = "Operation trade stocks.";
    public final String  SEARCH = "Search for stock";

    private ApplicationControl applicationControl;
}
