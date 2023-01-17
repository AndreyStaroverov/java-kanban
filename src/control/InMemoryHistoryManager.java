package control;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList customLinkedList = new CustomLinkedList();

 @Override
 public void add(Task task) {
     if (!getHistory().isEmpty()) {
         if (getHistory().contains(task)) {
             getHistory().remove(task);
             int id = task.getId();
             remove(id);
             customLinkedList.LinkLast(task);
         } else {
             customLinkedList.LinkLast(task);
         }
     } else {
         customLinkedList.LinkLast(task);
     }
     }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        getHistory().remove(id);
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

      void LinkLast(Task task) {
          int id = task.getId();
          final Node oldtail = tail;
          final Node newtail = new Node(oldtail, task, null);
          tail = newtail;
          if (oldtail == null) {
              head = newtail;
              customLinkedMap.put(id, newtail);
          } else {
              oldtail.next = newtail;
              customLinkedMap.put(id, newtail);
          }

      }


      List<Task> getTasks() {
          List<Task> historyTasks = new ArrayList<>();
          Node currentNode = head;

          if (currentNode == null) {
              return historyTasks;
          } else {
              while (currentNode.next != null) {
                  historyTasks.add(currentNode.data);
                  currentNode = currentNode.next;
              }
              historyTasks.add(currentNode.data);
          }
          return historyTasks;
      }

      void removeNode(Node node) {
          int idNode = node.data.getId();
          customLinkedMap.remove(idNode);
          Node currentNode = head;
          Node prevNode = null;

          while (currentNode.next != null) {
              if (currentNode == node) {
                  if (currentNode == head) {
                      head = currentNode.next;
                  } else {
                      prevNode.next = currentNode.next;
                  }
              }
              prevNode = currentNode;
              currentNode = currentNode.next;
          }
      }
  }
