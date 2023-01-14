import java.util.Iterator;

// Represents a Deque
public class Deque<T> implements Iterable<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  // Creates a new iterator for this Deque
  public Iterator<T> iterator() {
    return new DequeIterator<T>(this.header);
  }

  // Determines the number of nodes that this Deque contains
  int size() {
    return this.header.size();
  }

  // Finds the first node to satisfy the predicate, returns the header if none do
  ANode<T> find(IPred<T> predicate) {
    return header.find(predicate, true);
  }

  // Adds a Node containing data of type T at the head of this Deque
  T addToHead(T t) {
    return header.addToHead(t);
  }

  // Adds a Node containing data of type T at the tail of this Deque
  T addToTail(T t) {
    return header.addToTail(t);
  }

  // Removes the Node that is at the head of this Deque
  T removeFromHead() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty list");
    }
    return header.removeFromHead();
  }

  // Removes the Node that is at the tail of this Deque
  T removeFromTail() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty list");
    }
    return header.removeFromTail();
  }

  // EFFECT: Removes any given Node from this Deque
  void remove(ANode<T> node) {
    if (!node.equals(header)) {
      header.removeHelper(node);
    }
  }
}
