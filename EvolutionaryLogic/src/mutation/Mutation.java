package mutation;

public interface Mutation<T> {
    void mutate(T child);
}
