// Function object that determines whether one of the Nodes is less than 10
public class HasLessThan10 implements IPred<Double> {

  // Applies the predicate
  public boolean apply(Double d) {
    return d < 10;
  }
}
