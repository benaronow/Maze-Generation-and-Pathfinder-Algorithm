import java.util.ArrayList;

// Represents a Stack
public class Stack<T> {
  Deque<T> allData;

  Stack() {
    this.allData = new Deque<T>();
  }

  Stack(ArrayList<T> ts) {
    allData = new Deque<T>();
    for (T t : ts) {
      allData.addToTail(t);
    }
  }

  // Determines if the list contains any data
  boolean isEmpty() {
    return allData.size() == 0;
  }

  // EFFECT: Stacks an item at head of the list of data
  void push(T item) {
    allData.addToHead(item);
  }

  // Removes and returns the item at the head of the list of data
  T pop() {
    return allData.removeFromHead();
  }
}
