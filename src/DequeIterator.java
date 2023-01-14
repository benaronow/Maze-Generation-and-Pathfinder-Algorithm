import java.util.Iterator;

// Represents an Iterator for a Deque
public class DequeIterator<T> implements Iterator<T> {
  ANode<T> node;

  DequeIterator(ANode<T> node) {
    this.node = node;
  }

  // Determines whether the Deque has a next item
  public boolean hasNext() {
    return node.hasNext();
  }

  // Returns the value of the next item of the Deque
  public T next() {
    T item = this.node.nextNode.getData();
    this.node = this.node.nextNode;
    return item;
  }
}
