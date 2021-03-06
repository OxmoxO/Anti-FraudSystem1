package antifraud.model.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Field<T> implements Predicate<T> {
    private final Function<T, Object> function;
    private final Set<Object> seenObjects;

    public Field(Function<T, Object> function) {
        this.function = function;
        this.seenObjects = new HashSet<>();
    }
    public boolean test(T t) {
        return seenObjects.add(function.apply(t));
    }
}
