# Dream tracker

## Functional Requirements:

___
### Security oriented
* User is able to safely register an account
  * Using google account - oauth 
  * Using facebook account
  * Using github account  
  * Using email of choice
    * In this case email needs to be confirmed via verification mail

* User is able to safely authenticate
  * Using google account
  * Using github account 
  * Using previously registered account if correct credentials were provided

* User is able to change his credentials if the registration with email of choice were selected
* User is able to change his password in case of forgetting if registration with email of choice were selected via email with link to change password form
___
### Habit oriented
* User is able to create Habit to track
    * User is able to define action - habit itself 
    * User is able to define frequency of habit(Daily/Weekly/Monthly)
    * User is able to define duration of a habit
    * User is able to attach category to a habit(in predefined set of categories or created one)
    * User is able to attach habit to a certain goal
    * User is able to define difficulty of habit(Easy/Medium/Hard)
    * User is able to add other people to habit or goal via email or username
    * User is able to define status of a habit(Active/Inactive)
* User is able to create goal
  * User is able to define list of habits which goal consist of 
  * User is able to define amount of done habits to finish a goal
  * User is able to define duration of the goal
* User is able to see his progress through charts 
  * User is able to switch views of charts
    * Connected with certain goal 
    * Connected with certain category
    * Connected with certain habit
    * Connected with habit of choice 
* User is able to apply predefined habit creation schema for habit of choice
* User is able to attach habit to own profile page
* User is able to mark habit as done or undone for specified time interval 
  * Properly marked habit is being count to goal if attached and it counts to habit statistics chart
* User is able to browse through active and inactive habits 
___
### Interface oriented 
* User is able to create his own view of of Habit tracking(by moving habit blocks)
* User is able to use predefined set of views
  * Habit oriented
  * Goal oriented
  * Category oriented
___

### Sequence diagrams

### New user without habits and goals
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Get habits GET /habits
  API ->> DB: Get habits select * from habits where user_id = :user_id
  DB -->> API: 0 rows
  API -->> User: Habits []
  
  User ->> API: Get goals GET /goals
  API ->> DB: Get goals select * from goals where user_id = :user_id
  DB -->> API: 0 rows
  API -->> User: Goals []
```

### User sets up a goal
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Create goal POST /goals
  API ->> DB: Create goal insert into goals values (goal)
  DB -->> API: Goal 
  API -->> User: Goal
```

### User sets up a habit
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Create habit POST /habits
  API ->> DB: Create habit insert into habits values (habit)
  DB -->> API: Habit 
  API -->> User: Habit
```

### User optionally assigns a habit to a goal
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Get the habit we want to assign GET /habits/:id
  API ->> DB: Get the habit we want to assign select * from habits where id = id and user_id = u:ser_id
  DB -->> API: Habit
  API -->> User: Habit
  User ->> API: Get the list of goals we want to assign habit to GET /goals?status=active
  API ->> DB: Get the list of goals we want to assign habit to select * from goals where user_id = :user_id and status = active
  DB -->> API: Goals
  API -->> User: Goals
  User ->> API: Assign habit to goal POST /goals/:id/habits 
  API ->> DB: Assign habit to goal insert into goals_habits values (goal_id, habit_id)
  DB -->> API: Goal
  API -->> User: Goal
```

### User assigns a custom category to the habit
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Get the habit we want to assign GET /habits
  API ->> DB: Get the habit we want to assign select * from habits where user_id = user_id
  DB -->> API: Habits
  API -->> User: Habits
  User ->> API: Get the list of the current user categories GET /categories
  API ->> DB: Get the list of the current user categories select * from categories where user_id = :user_id or user_id is null
  User ->> API: Create category POST /categories
  API ->> DB: Create category insert into categories values (category, user_id)
  DB -->> API: Category
  API -->> User: Category
  User ->> API: Assign category to habit POST /habits/:id/categories/:id
  API ->> DB: Assign category to habit insert into habits_categories values (habit_id, category_id)
  DB -->> API: Habit
  API -->> User: Habit
```

### User fetches habits, check the tracking of one of them and marks it as done
```mermaid
sequenceDiagram
  actor User
  participant API
  participant DB
  User ->> API: Get habits GET /habits
  API ->> DB: Get habits select * from habits where user_id = :user_id
  DB -->> API: Habits
  API -->> User: Habits
  User ->> API: Get habit tracking GET /habits-tracking?habit_id=:id
  API ->> DB: Get habit tracking select * from habits_tracking where habit_id = :id and user_id = :user_id
  DB -->> API: Tracking
  API -->> User: Tracking
  User ->> API: Mark habit tracking as done now POST /habits-tracking
  API ->> DB: Mark habit as done insert into habit_tracking values (habit_id, date, status)
  DB -->> API: Tracking
  API -->> User: Tracking
```