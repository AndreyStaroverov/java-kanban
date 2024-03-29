import Servers.HttpTaskServer;
import Servers.KVServer;
import Tasks.*;
import control.*;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        File file = new File(".\\resources\\HistorySaver.csv");

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        new KVServer().start();

        HttpTaskManager manager = (HttpTaskManager) Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Тест меню для проверки трекера задач на функционирование: id0/Task, id1/Epic, id3/SubTask");
        printMenu();
        outofwhile: while(scanner.hasNextInt()){
            int typing = scanner.nextInt();
            switch(typing){
                case 0:
                    break outofwhile;
                case 1:
                    System.out.println("Создали задачу");
                    String name = ("Задача 0");
                    String description = "Описание задачи...";

                    //Task task = new Task(name, description, StatusOfTask.NEW);
                    Task task = new Task(name, description, StatusOfTask.NEW, TypeOfTask.TASK,
                            LocalDateTime.of(2022, 10,10,12,30,0), 30);
                    manager.createTask(task);

                    printMenu();
                    break;
                case 2:
                    System.out.println("Создали новую подзадачу");
                    String name1 = ("Подзадача 1");
                    String description1 = "Описание подзадачи...";
                    int epicId = 1;
                    //Subtask subtask1 = new Subtask(name1, description1, StatusOfTask.NEW, epicId);
                    Subtask subtask1 = new Subtask(name1, description1, StatusOfTask.NEW,epicId,TypeOfTask.SUBTASK,
                            LocalDateTime.of(2022,10,5,16,50,0), 15);
                    manager.createSubtask(subtask1, epicId);

                    printMenu();
                    break;
                case 3:
                    System.out.println("Создали новый эпик");
                    String name2 = ("Эпик 1");

                    Epic epic = new Epic(name2, StatusOfTask.NEW);
                    manager.createEpic(epic);

                    printMenu();
                    break;
                case 4:
                    System.out.println(manager.getTasks());
                    System.out.println(manager.getSubTasks());
                    System.out.println(manager.getEpics());

                    System.out.println("Список подзадач для эпика");
                    System.out.println(manager.getEpicSubtasks(1));

                    printMenu();
                    break;
                case 5:
                    System.out.println("Update задачу");
                    String nameUp = ("Задача 0");
                    String descriptionUp = "Описание задачи...";

                    Task taskUp = new Task(nameUp, descriptionUp, StatusOfTask.IN_PROGRESS, 0);

                    manager.updateTask(taskUp);
                    printMenu();
                    break;
                case 6:
                    System.out.println("Обновляем эпик 1");
                    String name2Up = ("Эпик 1");
                    List<Subtask> subTaskList =  manager.getEpicSubtasks(1);

                    Epic epicUp = new Epic(name2Up, subTaskList, 1);
                    manager.updateEpic(epicUp);

                    printMenu();
                    break;
                case 7:
                    System.out.println("Update подзадачу");
                    String name1Up = ("Подзадача 1");
                    String description1Up = "Описание подзадачи...";
                    epicId = 1;

                    Subtask subtaskUp = new Subtask(name1Up, description1Up, StatusOfTask.DONE, epicId, 2);
                    manager.updateSubtask(epicId, subtaskUp);

                    printMenu();
                    break;
                case 8:
                    int taskId = 0; //0
                    manager.deleteTask(taskId);
                    printMenu();
                    break;
                case 9:
                    int subtaskId = 2; //2
                    manager.deleteSubtask(subtaskId);
                    printMenu();
                    break;
                case 10:
                    int epicIde = 1; //1
                    manager.deleteEpic(epicIde);
                    printMenu();
                    break;
                case 11:
                    manager.getTaskById(0);
                    break;
                case 12:
                    manager.getSubTaskById(2);
                    break;
                case 13:
                    manager.getEpicById(1);
                    break;
                case 14:
                    System.out.println(manager.getHistory());
                    break;
                case 15:
                    System.out.println(manager.getPrioritizedTasks());
                    break;
                case 16:
                    HttpTaskManager manager1 = new HttpTaskManager(URI.create("http://localhost:8078"));
                    System.out.println(manager1.getHistory());
                    System.out.println(manager1.getPrioritizedTasks());
                    System.out.println(manager1.getTasks());
                    break;
                default:
                    System.out.println("Выбор неверный.");
            }
        }
    }


    public static void printMenu(){
        System.out.println("1 - Создать sprint3.Task ");
        System.out.println("2 - Создать sprint3.Subtask");
        System.out.println("3 - Создать sprint3.Epic");
        System.out.println("4 - Напечатать список");
        System.out.println("5 - Обновить sprint3.Task ");
        System.out.println("6 - Обновить Эпик");
        System.out.println("7 - Обновить SubTask");
        System.out.println("8 - Удалить sprint3.Task ");
        System.out.println("9 - Удалить sprint3.Subtask");
        System.out.println("10 - Удалить sprint3.Epic");
        System.out.println("11 - Получить Таск для проверки истории");
        System.out.println("12 - Получить Сабтаск для проверки истории");
        System.out.println("13 - Получить Эпик для проверки истории");
        System.out.println("14 - Вывести историю");
        System.out.println("15 - Вывести задачи в приоритете");
        System.out.println("16 - start HTTP load from sever");
        System.out.println("0 - выход");
   }
}

