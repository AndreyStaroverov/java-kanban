package control;

import Servers.HttpTaskServer;
import Servers.KVServer;
import exception.ManagerSaveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnNotNullTaskManagerGetDefault() throws IOException {
        assertThrows(ManagerSaveException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                TaskManager tm = Managers.getDefault();
                 assertNotNull(tm);
            }
        });

    }

    @Test
    public void shouldReturnNotNullHistoryManagerGetDefault(){
        HistoryManager hm = Managers.getDefaultHistory();
        assertNotNull(hm);
    }

    @Test
    public void shouldReturnNotNullFileBucketTaskManager(){
       // File file = new File(".\\resources\\HistorySaver.csv");
        FileBackedTasksManager fbtm = Managers.getFileBackedTaskManager();
        assertNotNull(fbtm);
    }
}