// Represents a predicate applied to a value of type T depending on its class
public interface IPred<T> {
  boolean apply(T t);
}
