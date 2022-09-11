package com.example.demounittes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * @author thangdt
 */
class Tests {


    int sum(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException(" a > 0 va b > 0");
        }
        return a + b;
    }

    @Test
    void checkSumTwoNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sum(-5, 10);
        });
        Assertions.assertEquals(15, sum(5, 10));
    }

}
