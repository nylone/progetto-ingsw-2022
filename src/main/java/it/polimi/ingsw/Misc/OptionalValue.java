package it.polimi.ingsw.Misc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class takes inspiration from {@link java.util.Optional} but adds serialization support and foregoes some
 * lesser used methods in some instances.
 * While {@link java.util.Optional} was never meant to be used as a persistent value in memory, the layer of abstraction
 * it provides over null values was valuable enough to warrant a rewrite into an object that could be Serialized and shared
 * over the net.
 *
 * @param <T> the internal type of the OptionalValue
 */
public class OptionalValue<T> implements Serializable {
    private final T value;

    /**
     * Private constructor for the object
     */
    private OptionalValue() {
        this.value = null;
    }

    /**
     * Private constructor for the object
     */
    private OptionalValue(T value) {
        this.value = value;
    }

    /**
     * Creates a new wrapping around a non null value
     *
     * @param element non null element to wrap an optional value around
     * @param <T>     the type of element stored inside the Optional
     * @return the {@link OptionalValue} containing element
     */
    public static <T> OptionalValue<T> of(T element) {
        Objects.requireNonNull(element);
        return new OptionalValue<>(element);
    }

    /**
     * Creates a new wrapping around a value
     *
     * @param element possibly null value to wrap this object around
     * @param <T>     The type of value stored inside the optional value
     * @return an empty {@link OptionalValue} if element is null, otherwise an {@link OptionalValue} wrapping element
     */
    public static <T> OptionalValue<T> ofNullable(T element) {
        if (element == null) {
            return new OptionalValue<>();
        }
        return new OptionalValue<>(element);
    }

    /**
     * Check if no value is present inside the optional
     *
     * @return true if the internal value is absent
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Check if a value is present inside the optional
     *
     * @return true if the internal value is present
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Retrieve the inner value
     *
     * @return the inner value of the optional, if present
     * @throws NullPointerException if no inner value is present
     */
    public T get() {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    /**
     * Retrieve the inner value
     *
     * @param otherwise the default value to return if no inner value is present
     * @return the inner value of the optional, if present. Otherwise returns the input value
     */
    public T orElse(T otherwise) {
        if (value == null) {
            return otherwise;
        }
        return value;
    }

    /**
     * If a value is present, run the provided consumer over it
     *
     * @param consumer a function to run over the contained value (if present)
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    /**
     * If a value is present, returns the result of applying the given Optional-bearing mapping function to the value, otherwise returns an empty OptionalValue.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U>    The type of value of the Optional returned by the mapping function
     * @return the result of applying an Optional-bearing mapping function to the value of this Optional, if a value is present, otherwise an empty Optional
     */
    public <U> OptionalValue<U> flatMap(Function<? super T, OptionalValue<U>> mapper) {
        if (value == null) {
            return OptionalValue.empty();
        }
        return mapper.apply(value);
    }

    /**
     * Get an empty value instance
     *
     * @param <T> The type of value stored inside the optional value
     * @return an empty {@link OptionalValue}
     */
    public static <T> OptionalValue<T> empty() {
        return new OptionalValue<>();
    }

    /**
     * Returns the hash code of the value, if present, otherwise 0 (zero) if no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Indicates whether some other object is "equal to" this OptionalValue. The other object is considered equal if:
     * <ul>
     *     <il>it is also an Optional and;</il>
     *     <il>both instances have no value present or;</il>
     *     <il>the present values are "equal to" each other via equals().</il>
     * </ul>
     *
     * @param o the object to be tested for equality
     * @return true if the two objects are considered equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionalValue<?> optional)) return false;
        return Objects.equals(value, optional.value);
    }
}
