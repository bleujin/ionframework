package net.ion.framework.util;

public interface Action<T> {
    void invoke(T result);
}
