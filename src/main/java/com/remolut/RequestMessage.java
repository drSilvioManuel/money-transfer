package com.remolut;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Objects;

public class RequestMessage {

    public interface General {

        int getId();

        Operation getOperation();
    }

    @Immutable
    public static class Update implements General {

        private final int id;
        private final Operation operation;

        public Update(final int id, final Operation operation) {
            this.id = id;
            this.operation = operation;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public Operation getOperation() {
            return operation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Update update = (Update) o;
            return id == update.id &&
                    Objects.equals(operation, update.operation);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, operation);
        }
    }

    @Immutable
    public static class Transfer implements General {

        private final int id;
        private final int idTo;
        private final Operation operation;

        public Transfer(final int from, final int to, final Operation operation) {
            this.id = from;
            this.idTo = to;
            this.operation = operation;
        }

        @Override
        public int getId() {
            return id;
        }

        public int getIdTo() {
            return idTo;
        }

        @Override
        public Operation getOperation() {
            return operation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transfer transfer = (Transfer) o;
            return id == transfer.id &&
                    idTo == transfer.idTo &&
                    Objects.equals(operation, transfer.operation);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, idTo, operation);
        }
    }
}
