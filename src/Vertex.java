import java.util.ArrayList;

// Represents a vertex of a graph
public class Vertex {
  int x;
  int y;
  ArrayList<Edge> adjacentEdges;
  boolean seen;
  boolean onPath;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.adjacentEdges = new ArrayList<>();
    this.seen = false;
    this.onPath = false;
  }

  // A hash equation for identifying each vertex
  int vertexHash() {
    return 1000 * y + x;
  }
}
