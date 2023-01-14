import java.util.ArrayList;
import java.util.HashMap;

//Represents Breadth-First Search
public class BFS extends ASearch {
  Queue<Vertex> worklist;

  BFS(ArrayList<Vertex> loVertex) {
    this.worklist = new Queue<>();
    worklist.enqueue(loVertex.get(0));
    loVertex.get(0).seen = true;
    pathTracker = new HashMap<>();
  }

  // Determines whether the Queues have a next item
  public boolean hasNext() {
    return !worklist.isEmpty();
  }

  // Returns the value of the next item of the Queue after iterating through
  // one step of the BFS
  public Queue<Vertex> next() {
    Vertex vertex = worklist.dequeue();
    for (Edge edge : vertex.adjacentEdges) {
      if (!edge.to.seen) {
        pathTracker.put(edge.to.vertexHash(), edge.from);
        if (edge.to.x == MazeWorld.width - 1 && edge.to.y == MazeWorld.height - 1) {
          buildPath(pathTracker, edge.to);
          worklist = new Queue<>();
        }
        else {
          edge.to.seen = true;
          worklist.enqueue(edge.to);
        }
      }
    }
    return worklist;
  }
}
