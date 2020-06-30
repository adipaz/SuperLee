package PresentationLayer;

import java.util.List;

public interface IQuery<T> {
    List<T> deployQuery(String... arg);
}
