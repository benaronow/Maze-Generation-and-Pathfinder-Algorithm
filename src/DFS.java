import java.util.ArrayList;
import java.util.HashMap;

//Represents a Depth-First Search
public class DFS extends ASearch {
  Stack<Vertex> worklist;

  DFS(ArrayList<Vertex> loVertex) {
    this.worklist = new Stack<>();
    worklist.push(loVertex.get(0));
    loVertex.get(0).seen = true;
    pathTracker = new HashMap<>();
  }

  // Determines whether the Queues has a next item
  public boolean hasNext() {
    return !worklist.isEmpty();
  }

  // Returns the value of the next item of the Stack after iterating through
  // one step of the DFS
  public Stack<Vertex> next() {
    Vertex vertex = worklist.pop();
    for (Edge edge : vertex.adjacentEdges) {
      if (!edge.to.seen) {
        pathTracker.put(edge.to.vertexHash(), edge.from);
        if (edge.to.x == MazeWorld.width - 1 && edge.to.y == MazeWorld.height - 1) {
          buildPath(pathTracker, edge.to);
          worklist = new Stack<>();
        }
        else {
          worklist.push(vertex);
          edge.to.seen = true;
          worklist.push(edge.to);
          break;
        }
      }
    }
    return worklist;
  }
}
