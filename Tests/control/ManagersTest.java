package control;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnNotNullTaskManagerGetDefault(){
        TaskManager tm = Managers.getDefault();
        assertNotNull(tm);
    }

    @Test
    public void shouldReturnNotNullHistoryManagerGetDefault(){
        HistoryManager hm = Managers.getDefaultHistory();
        assertNotNull(hm);
    }

    @Test
    public void shouldReturnNotNullFileBucketTaskManager(){
        File file = new File(".\\resources\\HistorySaver.csv");
        FileBackedTasksManager fbtm = Managers.getFileBackedTaskManager(file);
        assertNotNull(fbtm);
    }
}