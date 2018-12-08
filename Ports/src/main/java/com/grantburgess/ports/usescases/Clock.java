package com.grantburgess.ports.usescases;

import java.time.LocalDate;

public interface Clock {
    default LocalDate now() {
        return LocalDate.now();
    }
}
