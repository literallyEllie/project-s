package com.elliegabel.s.http.util;

import io.javalin.http.BadRequestResponse;

import java.util.function.Predicate;

public class Validate {

    public static void require(boolean predicate, String error) {
        if (!predicate) {
            throw new BadRequestResponse(error);
        }
    }

    public static void requireString(String field, String input) {
        require(input != null && !input.isBlank(), field + " must not be blank");
    }

}
