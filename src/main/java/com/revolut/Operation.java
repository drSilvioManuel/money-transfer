package com.revolut;

import com.revolut.Exception.InvalidOperationException;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Operation {

    final double money;

    public Operation(double m) {
        if (m < 0) throw new InvalidOperationException(
                new JsonError(
                        "Operation error",
                        "The money amount has to be positive value"));
        money = m;
    }

    Operation() {
        this(0);
    }

    public double getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Double.compare(operation.money, money) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(money);
    }
}
