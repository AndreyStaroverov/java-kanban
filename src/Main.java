import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.StatusOfTask;
import control.*;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
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

                    Task task = new Task(name, description, StatusOfTask.NEW);
                    manager.createTask(task);

                    printMenu();
                    break;
                case 2:
                    System.out.println("Создали новую подзадачу");
                    String name1 = ("Подзадача 1");
                    String description1 = "Описание подзадачи...";
                    int epicId = 1;
                    Subtask subtask1 = new Subtask(name1, description1, StatusOfTask.NEW, epicId);
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
                    ArrayList<Subtask> subTaskList =  manager.getEpicSubtasks(1);

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
                    int taskId = 0;
                    manager.deleteTask(taskId);
                    printMenu();
                    break;
                case 9:
                    int subtaskId = 2;
                    manager.deleteSubtask(subtaskId);
                    printMenu();
                    break;
                case 10:
                    int epicIde = 1;
                    manager.deleteEpic(epicIde);
                    printMenu();
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
        System.out.println("0 - выход");
    }
}

