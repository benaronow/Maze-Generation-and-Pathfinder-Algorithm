import java.util.Comparator;

// Compares two edges by their weight
public class CompareEdges implements Comparator<Edge> {

  CompareEdges() {}

  // Returns positive if the second edge has a larger weight
  public int compare(Edge e1, Edge e2) {
    return e2.weight - e1.weight;
  }
}
