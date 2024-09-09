# Dream tracker - General docs

The Dream Tracker application was created to develop habits, monitor progress in completing them, and achieve set goals. Its main purpose is to track progress based on statistics calculated by the application, presented in an accessible way. This project was designed for users who want to work on themselves systematically and introduce changes into their daily lives.

## Functional Requirements:


### Security oriented
* User is able to safely register an account
    * Using email of choice
        * In this case email needs to be confirmed via verification mail
* User is able to safely authenticate
    * Using previously registered account if correct credentials were provided
* User is able to change his password in case of forgetting if registration with email of choice were selected via email with link to change password form
___
### Habit oriented
* User is able to create Habit to track
* User is able to define action - habit itself
* User is able to define frequency of habit(Daily/Weekly/Monthly)
* User is able to define duration of a habit
* User is able to attach habit to a certain goal
* User is able to define difficulty of habit(Easy/Medium/Hard)
* User is able to mark habit as completed
* User is able to update habit details
* User is able to delete habit
* User is able to mark habit as done or undone for specified time interval
    * Properly marked habit is being count to goal if attached and it counts to habit statistics chart
### Goal oriented
* User is able to create goal
    * User is able to define habit which goal consist of
    * User is able to define amount of done habits to finish a goal
    * User is able to define duration of the goal
    * User is able to delete goal
### Charts oriented
* User is able to see individual habit progress through charts
   * User is able to see average break between each habit completion
   * User is able to success rate of habit tracking for each day of the week
   * User is able to see number of done and undone habits and calculated trend based on that
   * User is able to see the longest consecutive habit tracking streak
* User is able to see  number of done habits per day in monthly time frame
___
### View oriented
* User is able to create his own view of of Habit tracking(by moving blocks)
* User is able to save block position
* User is able to reset block position
* User is able to turn off/turn on moving blocks visibility 

___

```mermaid
sequenceDiagram
    participant User
    participant DomainCategoryService
    participant CategoryRepositoryPort
    participant CurrentUserProvider

    User->>DomainCategoryService: createCategory(CategoryRequest)
    DomainCategoryService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainCategoryService: userUUID
    DomainCategoryService->>CategoryRepositoryPort: save(categoryToCreate)
    CategoryRepositoryPort-->>DomainCategoryService: categorySavedToDB
    DomainCategoryService-->>User: CategoryResponse

    User->>DomainCategoryService: delete(UUID id)
    DomainCategoryService->>CategoryRepositoryPort: existsById(id)
    CategoryRepositoryPort-->>DomainCategoryService: true/false
    alt exists
        DomainCategoryService->>CategoryRepositoryPort: deleteById(id)
        DomainCategoryService-->>User: true
    else not exists
        DomainCategoryService-->>User: false
    end

    User->>DomainCategoryService: updateCategory(UUID id, CategoryRequest)
    DomainCategoryService->>CategoryRepositoryPort: findById(id)
    CategoryRepositoryPort-->>DomainCategoryService: foundCategory/exception
    alt found
        DomainCategoryService->>CategoryRepositoryPort: save(foundCategory)
        CategoryRepositoryPort-->>DomainCategoryService: categorySaveToDB
        DomainCategoryService-->>User: CategoryResponse
    else not found
        DomainCategoryService-->>User: exception
    end

    User->>DomainCategoryService: getAllUserCategories()
    DomainCategoryService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainCategoryService: userUUID
    DomainCategoryService->>CategoryRepositoryPort: findByUserUUID(userUUID)
    CategoryRepositoryPort-->>DomainCategoryService: listOfCategories
    DomainCategoryService-->>User: Page<CategoryResponse>
```
  
```mermaid
sequenceDiagram
    participant User
    participant DomainGoalService
    participant GoalRepositoryPort
    participant HabitRepositoryPort
    participant CurrentUserProvider

    User->>DomainGoalService: createGoal(GoalRequest)
    DomainGoalService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainGoalService: userUUID
    DomainGoalService->>HabitRepositoryPort: findById(goalRequest.habitID)
    HabitRepositoryPort-->>DomainGoalService: habitToBeAdded/exception
    alt found
        DomainGoalService->>GoalRepositoryPort: save(goalToCreate)
        GoalRepositoryPort-->>DomainGoalService: goalSavedToDB
        DomainGoalService->>HabitRepositoryPort: save(habitToBeAdded)
        HabitRepositoryPort-->>DomainGoalService: habitSavedToDB
        DomainGoalService-->>User: GoalResponse
    else not found
        DomainGoalService-->>User: exception
    end

    User->>DomainGoalService: delete(UUID id)
    DomainGoalService->>GoalRepositoryPort: findById(id)
    GoalRepositoryPort-->>DomainGoalService: goalResponse/exception
    alt found
        DomainGoalService->>HabitRepositoryPort: findById(goalResponse.getHabitUUID)
        HabitRepositoryPort-->>DomainGoalService: habitToBeAdded/exception
        alt found
            DomainGoalService->>DomainGoalService: removeGoalFromHabit(listOfGoals, goalResponse.getUuid)
            DomainGoalService-->>User: true
        else not found
            DomainGoalService-->>User: exception
        end
    else not found
        DomainGoalService-->>User: exception
    end

    User->>DomainGoalService: updateGoal(UUID id, GoalRequest)
    DomainGoalService->>GoalRepositoryPort: findById(id)
    GoalRepositoryPort-->>DomainGoalService: goalToUpdate/exception
    alt found
        DomainGoalService->>GoalRepositoryPort: save(goalToUpdate)
        GoalRepositoryPort-->>DomainGoalService: updatedGoal
        DomainGoalService-->>User: GoalResponse
    else not found
        DomainGoalService-->>User: exception
    end

    User->>DomainGoalService: getAllUserGoals()
    DomainGoalService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainGoalService: userUUID
    DomainGoalService->>GoalRepositoryPort: findByUserUUID(userUUID)
    GoalRepositoryPort-->>DomainGoalService: listOfGoals
    DomainGoalService-->>User: Page<GoalResponse>

    User->>DomainGoalService: getGoalById(UUID id)
    DomainGoalService->>GoalRepositoryPort: findById(id)
    GoalRepositoryPort-->>DomainGoalService: goal/exception
    alt found
        DomainGoalService-->>User: GoalResponse
    else not found
        DomainGoalService-->>User: exception
    end

    User->>DomainGoalService: increaseCompletionCount(UUID id)
    DomainGoalService->>GoalRepositoryPort: findById(id)
    GoalRepositoryPort-->>DomainGoalService: goal/exception
    alt found
        DomainGoalService->>GoalRepositoryPort: save(goal)
        GoalRepositoryPort-->>DomainGoalService: goalSavedToDb
        DomainGoalService-->>User: GoalResponse
    else not found
        DomainGoalService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainHabitService
    participant HabitRepositoryPort
    participant CurrentUserProvider
    participant StatsAggregator

    User->>DomainHabitService: delete(UUID id)
    DomainHabitService->>HabitRepositoryPort: existsById(id)
    HabitRepositoryPort-->>DomainHabitService: true/false
    alt exists
        DomainHabitService->>HabitRepositoryPort: deleteById(id)
        DomainHabitService-->>User: true
    else not exists
        DomainHabitService-->>User: false
    end

    User->>DomainHabitService: getHabitTrack(UUID id)
    DomainHabitService->>HabitTrackRepositoryPort: findByHabitUUID(id)
    HabitTrackRepositoryPort-->>DomainHabitService: listOfTracks
    DomainHabitService-->>User: List<HabitTrack>

    User->>DomainHabitService: createHabit(HabitRequest)
    DomainHabitService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainHabitService: userUUID
    DomainHabitService->>HabitRepositoryPort: save(habitToCreate)
    HabitRepositoryPort-->>DomainHabitService: habitSavedToDB
    DomainHabitService->>StatsAggregator: initializeAggregates(habitSavedToDB.getId)
    StatsAggregator-->>DomainHabitService: true
    DomainHabitService-->>User: HabitResponse

    User->>DomainHabitService: getAllUserHabits()
    DomainHabitService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainHabitService: userUUID
    DomainHabitService->>HabitRepositoryPort: findByUserUUID(userUUID)
    HabitRepositoryPort-->>DomainHabitService: habits
    DomainHabitService-->>User: Page<HabitResponse>

    User->>DomainHabitService: updateHabit(UUID id, HabitRequest)
    DomainHabitService->>HabitRepositoryPort: findById(id)
    HabitRepositoryPort-->>DomainHabitService: habitToUpdate/exception
    alt found
        DomainHabitService->>HabitRepositoryPort: save(habitToUpdate)
        HabitRepositoryPort-->>DomainHabitService: updatedHabit
        DomainHabitService-->>User: HabitResponse
    else not found
        DomainHabitService-->>User: exception
    end

    User->>DomainHabitService: linkCategoryWithHabit(UUID habitId, HabitCategoryCreateRequest)
    DomainHabitService->>HabitRepositoryPort: findById(habitId)
    HabitRepositoryPort-->>DomainHabitService: habitToLinkCategory/exception
    alt found
        DomainHabitService->>CategoryRepositoryPort: findById(categoryCreateRequest.id)
        CategoryRepositoryPort-->>DomainHabitService: categoryToBeLinked/exception
        alt found
            DomainHabitService->>HabitRepositoryPort: save(habitToLinkCategory)
            HabitRepositoryPort-->>DomainHabitService: habitSavedToDB
            DomainHabitService->>CategoryRepositoryPort: save(categoryToBeLinked)
            CategoryRepositoryPort-->>DomainHabitService: categorySavedToDB
        else not found
            DomainHabitService-->>User: exception
        end
    else not found
        DomainHabitService-->>User: exception
    end

    User->>DomainHabitService: getHabitById(UUID habitUUID)
    DomainHabitService->>HabitRepositoryPort: findById(habitUUID)
    HabitRepositoryPort-->>DomainHabitService: foundHabit/exception
    alt found
        DomainHabitService-->>User: HabitResponse
    else not found
        DomainHabitService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainHabitTrackService
    participant HabitTrackRepositoryPort
    participant HabitRepositoryPort
    participant StatsAggregator
    participant DomainGoalService

    User->>DomainHabitTrackService: getAllTracksOfHabit(UUID id)
    DomainHabitTrackService->>HabitTrackRepositoryPort: findByHabitUUID(id)
    HabitTrackRepositoryPort-->>DomainHabitTrackService: listOfTracks
    DomainHabitTrackService-->>User: Page<HabitTrackResponse>

    User->>DomainHabitTrackService: trackTheHabit(HabitTrackingRequest)
    DomainHabitTrackService->>HabitRepositoryPort: findById(habitTrackingRequest.habitId)
    HabitRepositoryPort-->>DomainHabitTrackService: habitToUpdateTracking/exception
    alt found
        DomainHabitTrackService->>HabitTrackRepositoryPort: save(track)
        HabitTrackRepositoryPort-->>DomainHabitTrackService: trackSavedToDB
        DomainHabitTrackService->>StatsAggregator: requestStatsUpdated(habitToUpdateTracking.getId, habitTrackResponse)
        StatsAggregator-->>DomainHabitTrackService: CombinedComponentResponse
        alt status is DONE
            DomainHabitTrackService->>DomainGoalService: increaseCompletionCount(goal.getUuid)
            DomainGoalService-->>DomainHabitTrackService: GoalResponse
        end
        DomainHabitTrackService-->>User: HabitTrackResponse
    else not found
        DomainHabitTrackService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainDependingOnDayService
    participant DependingOnDayRepositoryPort

    User->>DomainDependingOnDayService: initializeAggregate(UUID habitUUID)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: save(dependingOnDay)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateSavedToDB
    DomainDependingOnDayService-->>User: StatsComponentResponse

    User->>DomainDependingOnDayService: updateAggregate(UUID habitId, HabitTrackResponse)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: findByHabitUUID(habitId)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateFoundByHabitUUID/exception
    alt found
        DomainDependingOnDayService->>DependingOnDayRepositoryPort: save(dependingOnDayAggregateFoundByHabitUUID)
        DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateSavedToDB
        DomainDependingOnDayService-->>User: StatsComponentResponse
    else not found
        DomainDependingOnDayService-->>User: exception
    end

    User->>DomainDependingOnDayService: getAggregate(UUID habitId)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: findByHabitUUID(habitId)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateFoundByHabitUUID/exception
    alt found
        DomainDependingOnDayService-->>User: StatsComponentResponse
    else not found
        DomainDependingOnDayService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainDependingOnDayService
    participant DependingOnDayRepositoryPort

    User->>DomainDependingOnDayService: initializeAggregate(UUID habitUUID)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: save(dependingOnDay)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateSavedToDB
    DomainDependingOnDayService-->>User: StatsComponentResponse

    User->>DomainDependingOnDayService: updateAggregate(UUID habitId, HabitTrackResponse)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: findByHabitUUID(habitId)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateFoundByHabitUUID/exception
    alt found
        DomainDependingOnDayService->>DependingOnDayRepositoryPort: save(dependingOnDayAggregateFoundByHabitUUID)
        DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateSavedToDB
        DomainDependingOnDayService-->>User: StatsComponentResponse
    else not found
        DomainDependingOnDayService-->>User: exception
    end

    User->>DomainDependingOnDayService: getAggregate(UUID habitId)
    DomainDependingOnDayService->>DependingOnDayRepositoryPort: findByHabitUUID(habitId)
    DependingOnDayRepositoryPort-->>DomainDependingOnDayService: dependingOnDayAggregateFoundByHabitUUID/exception
    alt found
        DomainDependingOnDayService-->>User: StatsComponentResponse
    else not found
        DomainDependingOnDayService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainQuantityOfHabitsService
    participant QuantityOfHabitsAggregateRepositoryPort

    User->>DomainQuantityOfHabitsService: initializeAggregate(UUID habitUUID)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: save(quantityOfHabits)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabitsSaveToDB
    DomainQuantityOfHabitsService-->>User: StatsComponentResponse

    User->>DomainQuantityOfHabitsService: updateAggregate(UUID habitUUID, HabitTrackResponse)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: findByHabitUUID(habitUUID)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabits/exception
    alt found
        DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: save(quantityOfHabits)
        QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabitsSavedToDB
        DomainQuantityOfHabitsService-->>User: StatsComponentResponse
    else not found
        DomainQuantityOfHabitsService-->>User: exception
    end

    User->>DomainQuantityOfHabitsService: getAggregate(UUID habitId)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: findByHabitUUID(habitId)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabits/exception
    alt found
        DomainQuantityOfHabitsService-->>User: StatsComponentResponse
    else not found
        DomainQuantityOfHabitsService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainQuantityOfHabitsService
    participant QuantityOfHabitsAggregateRepositoryPort

    User->>DomainQuantityOfHabitsService: initializeAggregate(UUID habitUUID)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: save(quantityOfHabits)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabitsSaveToDB
    DomainQuantityOfHabitsService-->>User: StatsComponentResponse

    User->>DomainQuantityOfHabitsService: updateAggregate(UUID habitUUID, HabitTrackResponse)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: findByHabitUUID(habitUUID)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabits/exception
    alt found
        DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: save(quantityOfHabits)
        QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabitsSavedToDB
        DomainQuantityOfHabitsService-->>User: StatsComponentResponse
    else not found
        DomainQuantityOfHabitsService-->>User: exception
    end

    User->>DomainQuantityOfHabitsService: getAggregate(UUID habitId)
    DomainQuantityOfHabitsService->>QuantityOfHabitsAggregateRepositoryPort: findByHabitUUID(habitId)
    QuantityOfHabitsAggregateRepositoryPort-->>DomainQuantityOfHabitsService: quantityOfHabits/exception
    alt found
        DomainQuantityOfHabitsService-->>User: StatsComponentResponse
    else not found
        DomainQuantityOfHabitsService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainSingleDayService
    participant SingleDayAggregateRepositoryPort

    User->>DomainSingleDayService: initializeAggregate(UUID habitUUID)
    DomainSingleDayService->>SingleDayAggregateRepositoryPort: save(singleDayAggregate)
    SingleDayAggregateRepositoryPort-->>DomainSingleDayService: singleDayAggregateSavedToSB
    DomainSingleDayService-->>User: StatsComponentResponse

    User->>DomainSingleDayService: updateAggregate(UUID habitUUID, HabitTrackResponse)
    DomainSingleDayService->>SingleDayAggregateRepositoryPort: findByHabitUUID(habitUUID)
    SingleDayAggregateRepositoryPort-->>DomainSingleDayService: singleDayAggregate/exception
    alt found
        DomainSingleDayService->>SingleDayAggregateRepositoryPort: save(singleDayAggregate)
        SingleDayAggregateRepositoryPort-->>DomainSingleDayService: singleDayAggregateSaveToDB
        DomainSingleDayService-->>User: StatsComponentResponse
    else not found
        DomainSingleDayService-->>User: exception
    end

    User->>DomainSingleDayService: getAggregate(UUID habitId)
    DomainSingleDayService->>SingleDayAggregateRepositoryPort: findByHabitUUID(habitId)
    SingleDayAggregateRepositoryPort-->>DomainSingleDayService: singleDayAggregate/exception
    alt found
        DomainSingleDayService-->>User: StatsComponentResponse
    else not found
        DomainSingleDayService-->>User: exception
    end
```


```mermaid
sequenceDiagram
    participant User
    participant DomainStreakService
    participant StreakAggregateRepositoryPort

    User->>DomainStreakService: initializeAggregate(UUID habitUUID)
    DomainStreakService->>StreakAggregateRepositoryPort: save(streakAggregate)
    StreakAggregateRepositoryPort-->>DomainStreakService: streakAggregateSaveToDB
    DomainStreakService-->>User: StatsComponentResponse

    User->>DomainStreakService: updateAggregate(UUID habitUUID, HabitTrackResponse)
    DomainStreakService->>StreakAggregateRepositoryPort: findByHabitUUID(habitUUID)
    StreakAggregateRepositoryPort-->>DomainStreakService: streakAggregate/exception
    alt found
        DomainStreakService->>StreakAggregateRepositoryPort: save(streakAggregate)
        StreakAggregateRepositoryPort-->>DomainStreakService: streakAggregateSavedToDB
        DomainStreakService-->>User: StatsComponentResponse
    else not found
        DomainStreakService-->>User: exception
    end

    User->>DomainStreakService: getAggregate(UUID habitId)
    DomainStreakService->>StreakAggregateRepositoryPort: findByHabitUUID(habitId)
    StreakAggregateRepositoryPort-->>DomainStreakService: streakAggregate/exception
    alt found
        DomainStreakService-->>User: StatsComponentResponse
    else not found
        DomainStreakService-->>User: exception
    end
```

```mermaid
sequenceDiagram
    participant User
    participant DomainUserService
    participant UserRepositoryPort
    participant CurrentUserProvider

    User->>DomainUserService: createSampleUser()
    DomainUserService->>CurrentUserProvider: getCurrentUser()
    CurrentUserProvider-->>DomainUserService: userUUID
    DomainUserService->>UserRepositoryPort: save(sampleUser)
    UserRepositoryPort-->>DomainUserService: userSavedToDB
    DomainUserService-->>User: UserResponse

    User->>DomainUserService: findById(UUID uuid)
    DomainUserService->>UserRepositoryPort: findById(uuid)
    UserRepositoryPort-->>DomainUserService: user/exception
    alt found
        DomainUserService-->>User: Optional<User>
    else not found
        DomainUserService-->>User: Optional.empty
    end
```


```mermaid
sequenceDiagram
    participant User
    participant DomainViewService
    participant ViewRepositoryPort
    participant StatsAggregator

    User->>DomainViewService: getStatsComponent(String viewName)
    DomainViewService->>ViewRepositoryPort: findByName(viewName)
    ViewRepositoryPort-->>DomainViewService: foundView/exception
    alt found
        DomainViewService->>StatsAggregator: getAggregates(foundView.getHabitUUID)
        StatsAggregator-->>DomainViewService: CombinedComponentResponse
        DomainViewService-->>User: CombinedComponentResponse
    else not found
        DomainViewService-->>User: exception
    end

    User->>DomainViewService: createView(ViewRequest)
    DomainViewService->>ViewRepositoryPort: save(viewToSave)
    ViewRepositoryPort-->>DomainViewService: viewSavedToDB
    DomainViewService-->>User: ViewResponse
```
