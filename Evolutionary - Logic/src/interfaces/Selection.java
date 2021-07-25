package interfaces;

import java.util.List;

public interface Selection<T> {
    List<T> select(List<T> population);
}
