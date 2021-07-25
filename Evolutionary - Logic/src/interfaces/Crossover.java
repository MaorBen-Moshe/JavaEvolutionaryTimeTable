package interfaces;

import java.util.List;

public interface Crossover<T> {
    List<T> crossOver(List<T> parents);
}
