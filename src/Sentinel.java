// Represents a Sentinel Node
public class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.nextNode = this;
    this.prevNode = this;
  }

  // Returns nothing as this Sentinel contains no data
  T getData() {
    return null;
  }

  // Finds the first Node in the Deque that satisfies the predicate
  // where b is true only when the Sentinel has been passed through once
  ANode<T> find(IPred<T> predicate, boolean once) {
    return this.nextNode.find(predicate);
  }

  // Determines the number of Nodes in the Deque
  int size() {
    if (this.nextNode.equals(this)) {
      return 0;
    }
    return this.nextNode.size(this);
  }

  // Adds a Node containing data of type T to the head of the Deque
  T addToHead(T t) {
    new Node<T>(t, this.nextNode, this);
    return this.nextNode.getData();
  }

  // Adds a Node containing data of type T to the tail of the Deque
  T addToTail(T t) {
    new Node<T>(t, this, this.prevNode);
    return this.nextNode.getData();
  }

  // Removes the Node that is at the head of the Deque
  T removeFromHead() {
    return this.nextNode.remove();
  }

  // Removes the Node that is at the tail of the Deque
  T removeFromTail() {
    return this.prevNode.remove();
  }

  // EFFECT: Helper for remove methods when the Sentinel has been passed through
  // once
  void removeHelperOne(ANode<T> node) {
    this.nextNode.removeHelper(node);
  }

  // EFFECT: Helper for remove methods when the Sentinel has been passed through
  // twice
  void removeHelperTwo(ANode<T> node) {
    return;
  }

  // Determines if this Node is a Sentinel
  boolean isSentinel() {
    return true;
  }
}
