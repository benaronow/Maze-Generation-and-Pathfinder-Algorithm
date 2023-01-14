// Represents a standard Node
public class Node<T> extends ANode<T> {
  T data;

  Node(T data, ANode<T> nextNode, ANode<T> prevNode) {
    if ((nextNode == null) || (prevNode == null)) {
      throw new IllegalArgumentException("Cannot accept null node");
    }
    this.data = data;
    this.nextNode = nextNode;
    this.prevNode = prevNode;
    prevNode.nextNode = this;
    nextNode.prevNode = this;
  }

  // Retrieves the data contained in this Node
  T getData() {
    return this.data;
  }

  // Finds the first Node in the Deque that satisfies the predicate
  ANode<T> find(IPred<T> predicate) {
    if (predicate.apply(this.data)) {
      return this;
    }
    else {
      return this.nextNode.find(predicate);
    }
  }
}
