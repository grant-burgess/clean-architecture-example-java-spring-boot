package com.grantburgess.usecases;

import java.time.LocalDate;

public interface Clock {
    default LocalDate now() {
        return LocalDate.now();
    }
}
