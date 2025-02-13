# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 - Chess Server Design
[Sequence Diagram
](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZT6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco4LpOzz6K8SwgX0ALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAr5iK60pJpeCEKkqKq4RqBEsh6uqZLG-rTiGrHmpGHLRjAIkOhJEbwU68p7ihaDZrmjrOsmsEluhPIaQgea6SCSkAQMZGjFBNZBrOzbjq20HtqZhTZD2MD9oOvQWSOVkOdWU62Y29kTk5y6rt4fiBF4KDoKp9i+Mwx7pJkmBuReRTUNe0gAKK7jl9Q5c0LQPqoT7dNOdloM5bL-mm-SVcF87+W2dVsrhSEHolsLIQlvqYRiOFynhBL8aSMDkmAIkBkFc4UUybrUeUtEcCJMjaEKYSNXOoaUYt7GZcpiGyUG8bDXxe0CUYKDcMJQawnJ2jzWaEaFJaMg3RShiPfIu0LWxSngvFiWGVgvEHXV5ToSDaiaW1B0-lc35Xi5XY5GAfYDkOS6cBF66BJCtq7tCMAAOKjqyyWnml57MGZaYVKTBXFfYo4VbN6A1TpQLmdtIWOTBPO-odQPILE5OjKoPXQqzksDdhWmJqNl3jZN0182gz1UVJy08qtQbrfIm0wBrf0vZ2HUnXG2iKyNhv-eNYtgBLaiwlr+1vRyZMUmTo7yWNikcUdUMyxToOmOD3OpiHsSy2o4fw-TpQ9FMceqOMlT9HHACS0jWVMJ6ZAaFYtV8OgIKADbF8BpdPHHAByo6l3sMCNMjWWo+lGMeQOMDdKWqcUxnFRZ6Ouf530hf6pZY6hU85eV9Xflz4PoyN6MzetzjK6eJFG7YD4UDYNw8BCYYLspClZ7o+1KPlDeDQs2zwQc8+vQN6O7fHFHwJXH0GtQRbv3Uea8a6hUFqmW+nEYCej1C7WEsDMhx3lliW2F0HbugmhSdWr93ZsU9rrTga0GJbVfmbbWgMVI-WAGg-CKtMGIJQPAvBklKHHTgGfF24cFJLVkjaX2G8zoIU7JDU+XpMhcNhkZTAicg6XBASgcezcuanC7pjJwfckgpz6DnPOpcYDbzxlFAIlgbrIWSDAAAUhAHkAjDABAXiABsNMb5J0qFUSkd4Whx3ZnWJqQ5j7AFMVAOAEBkJQFmLor+HYf681foAzRpZngV2CaE8JE9dGAJkSZKBwcYAACsbFoHgYUnkki0SDVocrDBREpr3Q1iw167JCExlOhtRipseEI2GuUahVT7YvVqfA3RjTeErTscbF2udyH7TYeUF2QijroMGUxbAWgJGjlhLo98KTKBpOgKM7p0DrFlNHNwgOvDKRrL1HY2hsS0wnOKWcqRxkhYWzvqWaJndabqM0T0Qxu98YBC8EErsXpYDAGwMfQg8REiX2pl3XJ2U8oFSKq0YwXM-wmSRtkt5cyYHcDwAgwlWYUAVIVuDapKyQAkuJeClBhyCEfVut9Npv0ulMukJ9TIdoBQ2w5c05lX0JoUxmQDIOQMaX0peXcrFQtyhSrwAnHJRySg4pUa5H5PcNHY0wOFIAA)
