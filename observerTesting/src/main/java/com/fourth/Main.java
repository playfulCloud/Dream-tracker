package com.fourth;

import com.fourth.habit.MockHabit;

public class Main {
    public static void main(String[] args) {
        MockHabit mockHabit = new MockHabit();
        mockHabit.initMockView();
        mockHabit.simulateHabitTracking();
    }
}