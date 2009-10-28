package org.apache.expreval.util;

import java.util.Arrays;
import java.util.HashSet;

public class Sets {

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> HashSet<E> newHashSet(E... vals) {
        return new HashSet<E>(Arrays.asList(vals));
    }
}