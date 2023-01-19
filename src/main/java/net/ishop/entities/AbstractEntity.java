package net.ishop.entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<T> implements Serializable {
    T id;


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s [id = %s]", this.getClass().getSimpleName(), this.id);
    }
}
