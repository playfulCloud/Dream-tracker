package com.fourth.habit;

import com.fourth.habitStats.AverageBreakComponent;
import com.fourth.habitStats.StreakComponent;
import com.fourth.habitStats.TrendComponent;
import com.fourth.view.MockView;

import java.util.List;

public class MockHabit {

   MockView mockView = new MockView();


    List<String> mockOfHabitTracks;

   public void simulateHabitTracking(){
      mockView.notifyObservers();
   }



   public void initMockView(){
       mockView.addObserver(new AverageBreakComponent());
       mockView.addObserver(new StreakComponent());
       mockView.addObserver(new TrendComponent());
   }
}
