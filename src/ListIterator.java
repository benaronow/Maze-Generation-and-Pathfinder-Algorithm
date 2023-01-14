import java.util.ArrayList;
import java.util.Iterator;

// Represents an Iterator for an ArrayList
public class ListIterator<T> implements Iterator<T> {
  ArrayList<T> list;
  Iterator<T> iterator;

  ListIterator() {
    this.list = new ArrayList<T>();
    this.iterator = list.iterator();
  }

  // Determines whether the ArrayList has a next item
  public boolean hasNext() {
    return iterator.hasNext();
  }

  // Returns the value of the next item of the ArrayList
  public T next() {
    return iterator.next();
  }
}
