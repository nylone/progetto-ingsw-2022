package it.polimi.ingsw.Misc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class SerializableOptional<T> implements Serializable {
    private final T value;

    private SerializableOptional() {
        this.value = null;
    }

    private SerializableOptional(T value) {
        this.value = value;
    }

    public static <T> SerializableOptional<T> of(T element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return new SerializableOptional<>(element);
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

    public <U> SerializableOptional<U> flatMap(Function<? super T, SerializableOptional<U>> mapper) {
        if (value == null) {
            return SerializableOptional.empty();
        }
        return mapper.apply(value);
    }

    public static <T> SerializableOptional<T> empty() {
        return new SerializableOptional<>();
    }

    public <U> SerializableOptional<U> map(Function<? super T, ? extends U> mapper) {
        if (value == null) {
            return SerializableOptional.empty();
        }
        return SerializableOptional.ofNullable(mapper.apply(value));
    }

    public static <T> SerializableOptional<T> ofNullable(T element) {
        if (element == null) {
            return new SerializableOptional<>();
        }
        return new SerializableOptional<>(element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SerializableOptional<?> optional)) return false;
        return Objects.equals(value, optional.value);
    }
}
