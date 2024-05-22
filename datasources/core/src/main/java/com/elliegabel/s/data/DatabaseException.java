package com.elliegabel.s.data;

public class DatabaseException extends RuntimeException {

    public DatabaseException(Exception e) {
        super(e);
    }

    public DatabaseException() {
        super("database error");
    }
}
