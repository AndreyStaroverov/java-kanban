package control;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList customLinkedList = new CustomLinkedList();

 @Override
 public void add(Task task) {
     int id = task.getId();

     if (customLinkedList.head != null) {
         if (customLinkedList.getCustomLinkedMap().get(id) != null) {
             remove(id);
         }
     }
     customLinkedList.linkLast(task);
 }


    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = customLinkedList.getCustomLinkedMap().remove(id);
        customLinkedList.removeNode(node);
    }

}

  class CustomLinkedList {

      public Node head = null;
      public Node tail = null;

      public Map<Integer, Node> getCustomLinkedMap() {
          return customLinkedMap;
      }

      private final Map<Integer, Node> customLinkedMap = new HashMap<>();

      void linkLast(Task task) {
          int id = task.getId();
          final Node oldtail = tail;
          final Node newtail = new Node(oldtail, task, null);
          tail = newtail;
          if (oldtail == null) {
              head = newtail;
          } else {
              oldtail.next = newtail;
          }
          customLinkedMap.put(id, newtail);
      }


      List<Task> getTasks() {
          List<Task> historyTasks = new ArrayList<>();
          Node currentNode = head;

          if (currentNode == null) {
              return historyTasks;
          }
          while (currentNode.next != null) {
                  historyTasks.add(currentNode.data);
                  currentNode = currentNode.next;
              }
              historyTasks.add(currentNode.data);
          return historyTasks;
      }

      void removeNode(Node node) {
          customLinkedMap.remove(node.data.getId());
          Node currentNode = node;

          if (currentNode == head) {
              head = currentNode.next;
              currentNode.prev = null;
          } else if (currentNode == tail) {
              tail = currentNode.prev;
              currentNode.next = null;
          } else {
              currentNode.prev.next = currentNode.next;
              currentNode.next.prev = currentNode.prev;
          }
      }
  }
