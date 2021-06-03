package SystemEngine.tasks;

import SystemEngine.generated.RseStocks;
import SystemEngine.generated.RseUsers;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class FileLoaderTask extends Task<Boolean> {

    private final int SLEEP_TIME;
    private final String PATH;


    Consumer<Integer> updateStocksList;
    Consumer<Integer> loadFile;
    Consumer<Integer> updateUsersList;
    Consumer<String> exceptionHandling;

   public FileLoaderTask(String path, int sleepTime, Consumer<Integer> loadFile, Consumer<Integer> updateStocksList, Consumer<Integer> updateUsersList, Consumer<String> exceptionHandling){
        SLEEP_TIME = sleepTime;
        PATH = path;
        this.updateStocksList=updateStocksList;
        this.updateUsersList=updateUsersList;
        this.loadFile=loadFile;
        this.exceptionHandling=exceptionHandling;
   }

   @Override
    protected Boolean call() throws InterruptedException {

       //Load file
            updateMessage("Fetching file...");
            Thread.sleep(SLEEP_TIME);
            try {
                loadFile.accept(3);

                //Load stocks list
                updateMessage("Loading stock's List...");
                Thread.sleep(SLEEP_TIME);
                updateStocksList.accept(3);

                //Load user List
                updateMessage("Loading user's List...");
                Thread.sleep(SLEEP_TIME);
                updateUsersList.accept(3);
            }
            catch(Exception e){
                Platform.runLater(
                        () -> exceptionHandling.accept(e.getMessage()));
            return false;}
return true;
}
}
