package it.polimi.ingsw.Misc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class Optional<T> implements Serializable {
    private final T value;

    private Optional() {
        this.value = null;
    }

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return new Optional<>(element);
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    public T orElse(T otherwise) {
        if (value == null) {
            return otherwise;
        }
        return value;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        if (value == null) {
            return Optional.empty();
        }
        return mapper.apply(value);
    }

    public static <T> Optional<T> empty() {
        return new Optional<>();
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(value));
    }

    public static <T> Optional<T> ofNullable(T element) {
        if (element == null) {
            return new Optional<>();
        }
        return new Optional<>(element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Optional<?> optional)) return false;
        return Objects.equals(value, optional.value);
    }
}
