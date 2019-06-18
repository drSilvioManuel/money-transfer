package com.remolut;

import com.remolut.Exception.InvalidOperationException;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Operation {
    enum TYPE {
        DEPOSIT(1),
        WITHDRAW(2),
        TRANSFER(3),
        NONE(4);

        private int id;

        TYPE(int id) {
            this.id = id;
        }

        TYPE getType(int id) {
            for(TYPE e: TYPE.values()) {
                if(e.id == id) {
                    return e;
                }
            }
            return NONE;
        }

        public int getId() {
            return id;
        }
    }

    final TYPE type;
    final double money;

    private Operation(TYPE t, double m) {
        if (m < 0) throw new InvalidOperationException(
                new JsonError(
                        "Operation error",
                        "The money amount has to be positive value"));
        type = t;
        money = m;
    }

    public Operation(int t, double m) {
        this(TYPE.NONE.getType(t), m);
    }

    Operation() {
        this(TYPE.NONE, 0);
    }

    public TYPE getType() {
        return type;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Double.compare(operation.money, money) == 0 &&
                type == operation.type;
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, money);
    }
}
