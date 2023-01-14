// Function object that determines whether one of the Nodes has an "a"
public class HasLetterA implements IPred<String> {

  // Applies the predicate
  public boolean apply(String s) {
    return s.indexOf("a") > 0;
  }
}
