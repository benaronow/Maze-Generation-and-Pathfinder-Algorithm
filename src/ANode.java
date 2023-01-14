// Represents any type of Node
public abstract class ANode<T> {
  ANode<T> nextNode;
  ANode<T> prevNode;

  // Retrieves the data contained in this Node
  abstract T getData();

  // Finds the first Node in the Deque that satisfies the predicate
  ANode<T> find(IPred<T> predicate) {
    return this;
  }

  // Determines the number of Nodes in the Deque
  int size(ANode<T> node) {
    if (this.nextNode.equals(node)) {
      return 1;
    }
    else {
      return 1 + this.nextNode.size(node);
    }
  }

  // Removes this Node from the Deque
  T remove() {
    this.prevNode.nextNode = this.nextNode;
    this.nextNode.prevNode = this.prevNode;
    return this.getData();
  }

  // EFFECT: Helper method for remove
  void removeHelper(ANode<T> node) {
    if (this.equals(node)) {
      this.remove();
    }
    else {
      this.nextNode.removeHelper(node);
    }
  }

  // Determines whether this Node is a Sentinel
  boolean isSentinel() {
    return false;
  }

  // Determines whether this Node has another succeeding it
  boolean hasNext() {
    return !nextNode.isSentinel();
  }
}
