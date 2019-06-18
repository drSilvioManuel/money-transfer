package com.remolut;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Objects;

@Immutable
public class JsonError {

    private final String type;
    private final String message;

    public JsonError(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonError jsonError = (JsonError) o;
        return Objects.equals(type, jsonError.type) &&
                Objects.equals(message, jsonError.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, message);
    }
}
