import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Тест меню для проверки трекера задач на функционирование: ");
        printMenu();
        outofwhile: while(scanner.hasNextInt()){
            int typing = scanner.nextInt();
            switch(typing){
                case 0:
                    break outofwhile;
                case 1:
                    System.out.println("Создали задачу");
                    String name = ("Задача " + getRandomNumber(1,10));
                    String nameof = "Описание задачи...";

                    Task task = new Task(name, nameof, StatusOfTask.NEW);
                    manager.createTask(task);

                    printMenu();
                    break;
                case 2:
                    System.out.println("Создали новую подзадачу");
                    String name1 = ("Подзадача " + getRandomNumber(1,10));
                    String nameof1 = "Описание подзадачи...";
                    int epicId = 1;
                    Subtask subtask1 = new Subtask(name1, nameof1, StatusOfTask.NEW, epicId);
                    Subtask subtask2 = new Subtask(name1, nameof1, StatusOfTask.NEW, epicId);
                    manager.createSubtask(subtask1, epicId);
                    manager.createSubtask(subtask2, epicId);

                    printMenu();
                    break;
                case 3:
                    System.out.println("Создали новый эпик");
                    String name2 = ("Эпик " + getRandomNumber(1,10));

                    Epic epic = new Epic(name2, StatusOfTask.NEW);
                    manager.createEpic(epic);

                    printMenu();
                    break;
                case 4:
                    System.out.println(manager.printArrayAllTasks());
                    System.out.println(manager.printArrayAllSubTasks());
                    System.out.println(manager.printArrayEpic());

                    System.out.println("Список подзадач для эпика");
                    System.out.println(manager.printEpicSubtasks(1));

                    printMenu();
                    break;
                case 5:
                    System.out.println("Создали задачу");
                    String nameUp = ("Задача " + getRandomNumber(1,10));
                    String nameofUp = "Описание задачи...";

                    Task taskUp = new Task(nameUp, nameofUp, StatusOfTask.IN_PROGRESS);

                    manager.updateTask(taskUp);
                    printMenu();
                    break;
                case 6:
                    System.out.println("Создали новый эпик");
                    String name2Up = ("Эпик " + getRandomNumber(1,10));

                    Epic epicUp = new Epic(name2Up, StatusOfTask.IN_PROGRESS);
                    manager.updateEpic(epicUp);

                    printMenu();
                    break;
                case 7:
                    System.out.println("Создали новую подзадачу");
                    String name1Up = ("Подзадача " + getRandomNumber(1,10));
                    String nameof1Up = "Описание подзадачи...";
                    epicId = 1;
                    Subtask subtaskUp = new Subtask(name1Up, nameof1Up, StatusOfTask.DONE, epicId);

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
    } public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }



    public static void printMenu(){
        System.out.println("1 - Создать Task ");
        System.out.println("2 - Создать Subtask");
        System.out.println("3 - Создать Epic");
        System.out.println("4 - Напечатать список");
        System.out.println("5 - Обновить Task ");
        System.out.println("6 - Обновить Subtask");
        System.out.println("7 - Обновить Epic");
        System.out.println("8 - Удалить Task ");
        System.out.println("9 - Удалить Subtask");
        System.out.println("10 - Удалить Epic");
        System.out.println("0 - выход");
    }
}

