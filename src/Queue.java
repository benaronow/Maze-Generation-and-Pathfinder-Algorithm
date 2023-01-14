import java.util.ArrayList;

// Represents a Queue
public class Queue<T> {
  Deque<T> allData;

  Queue() {
    this.allData = new Deque<T>();
  }

  Queue(ArrayList<T> list) {
    allData = new Deque<T>();
    for (T data : list) {
      allData.addToTail(data);
    }
  }

  // Determines if the list contains any data
  boolean isEmpty() {
    return allData.size() == 0;
  }

  // EFFECT: Queues an item at the end of the list of data
  void enqueue(T item) {
    allData.addToTail(item);
  }

  // Removes and returns the item at the end of the list of data
  T dequeue() {
    return allData.removeFromTail();
  }
}
