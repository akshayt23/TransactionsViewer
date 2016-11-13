# TransactionsViewer
A simple Android app to view a set of products and associated transactions.

## Instructions to run the app
Place the `transactions.json` and `rates.json` file in the `assets` folder. Build and run the app.

## Architecture

The app is based on Uncle Bob's clean architecture, divided into three layers:

- **Domain layer**: This is where all the business logic along with the business objects of the application resides. 
This layer consistes of `UseCases` which are responsible for performing a single task. The business logic is executed in a 
background thread and the result is dispatched to the main thread. This layer is independent any Android dependencies. 

- **Data layer**: All data needed by the application comes via this layer through a `Repository`. A repository factory decides
where exactly to get the data from (cache/file/cloud). The origin of the data is thus transparent to the client.

- **Presentation layer**: This layer handles all the logic related to view and presentation. It uses the `MVP` architecture.
The `Presenters` talk to the `UseCases` and update the `View` accordingly.

## Libraries Used 
- Android Support Library
- JUnit and Mockito for testing
- Butterknife for view injection
- Dagger 2 for dependency injection
- JGraphT for graph data structures and algorithms

##

I'm building a conversion ratio graph from all the conversion rates provided.
If a direct conversion ratio is not available between two currencies, Dijkstra's algorithm is used to calculate the 
shortest path between those two currencies.

