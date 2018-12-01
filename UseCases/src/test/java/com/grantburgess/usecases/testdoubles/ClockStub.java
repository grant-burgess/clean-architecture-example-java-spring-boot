package com.grantburgess.usecases.testdoubles;

import com.grantburgess.usecases.Clock;

import java.time.LocalDate;

public class ClockStub implements Clock {
    private final LocalDate currentDate;

    public ClockStub(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDate now() {
        return currentDate;
    }
}
