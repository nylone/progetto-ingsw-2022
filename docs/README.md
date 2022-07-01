# Polimi Software Engineering project 2022 [ GC21 ]

Eriantys is the game chosen as the (2021/2022) project of the Software Engineering course at
[Polimi](https://www.polimi.it)

![Icon of the game](src/main/resources/icons/IconEriantys.png)

## Implemented Functionalities

| Functionality                             | Status |
|:------------------------------------------|:------:|
| Simplified ruleset                        |   ✅    |
| Advanced ruleset                          |   ✅    |
| Basic server requirements                 |   ✅    |
| Basic CLI requirements                    |   ✅    |
| Basic GUI requirements                    |   ✅    |
| All 12 Character cards                    |   ✅    |
| 2, 3 and 4 players game                   |   ✅    |
| Multiple games running on the same server |   ✅    |
| Public and private lobbies on server      |   ✅    |
| Persistence                               |   ❌    |
| Disconnection resilience                  |   ❌    |

#### Legend

`❌ Not Implemented` &nbsp; `✅ Implemented`

## Test coverage

Follow [this link](https://nylone.github.io/ing-sw-2022-rampone-rossi-salerno/coverage-report) for an unfiltered version
of the coverage report or follow [this link](https://nylone.github.io/ing-sw-2022-rampone-rossi-salerno/filtered-coverage-report)
for one of only Model and Controller.

## Javadocs

Follow [this link](https://nylone.github.io/ing-sw-2022-rampone-rossi-salerno/javadocs) to access the javadocs page for this project.

## Other Deliverables for the project review

All requested deliverables for use in the project review and assessment can be found 
[here](https://github.com/nylone/ing-sw-2022-rampone-rossi-salerno/tree/main/deliverables).

## Building and Running

Make sure you have the latest [**Java 18**](https://jdk.java.net/18/) jdk installed
before you attempt to build or run this project.

If you wish to build the package from source (on most Linux based distros):

```
git clone https://github.com/nylone/ing-sw-2022-rampone-rossi-salerno.git   # clone the repo
cd ing-sw-2022-rampone-rossi-salerno                                        # cd into the root of the repo
mvn clean install                                                           # tell maven to generate the package from source
```

Otherwise: you can download the latest release from the
[Packages](https://github.com/nylone?tab=packages&repo_name=ing-sw-2022-rampone-rossi-salerno) section
on the [Github](https://github.com/nylone/ing-sw-2022-rampone-rossi-salerno) page.

Assuming the release package is named `project.jar`, run the following command in the root directory of the
project to start the application:

```
java --enable-previw -jar project.jar 
```

Starting the application with no additional arguments will print a **help message**

The available command line options are the following:

|      Option | Function                                             | Maximum instances of same argument | Additional info                                                  |
|------------:|:-----------------------------------------------------|:----------------------------------:|:-----------------------------------------------------------------|
|         `s` | Start the **server**                                 |                 1                  | by default bound to `0.0.0.0:8080`                               |
|         `c` | Start the **CLI**                                    |                 1                  |                                                                  |
|         `g` | Start the **GUI**                                    |             unlimited              |                                                                  |
| `h` or `-h` | Prints out the help message                          |                 1                  | same message appears when app is called with incorrect arguments |
|        `-d` | Enables the **logger**                               |                 1                  | by default no logging is done                                    |
|    `-local` | Force the **server** to bind to a loopback interface |                 1                  | default bound address is `0.0.0.0`                               |
| `-port:XYZ` | Force the **server** to bind to port XYZ             |                 1                  | default port is `8080`                                           |

*Note*: the only argument that can be repeated in the call to the application is the one to start the GUI, in case the
user
wants to run multiple interfaces with one call to the application.

*Note*: we advise users running a CLI to not enable the `-d` flag as it can clutter the UI and makes for a terrible
game experience.

# Tools and Dependencies

| Name                                                                   |        Purpose        |
|:-----------------------------------------------------------------------|:---------------------:|
| [IntelliJ IDEA](https://www.jetbrains.com/idea/)                       | IDE and UML diagrams  |
| [Astah UML](https://astah.net/products/astah-uml/)                     |     UML diagrams      |
| [Java 18](https://jdk.java.net/18/)                                    |     Java release      |
| [Java Swing Library](https://docs.oracle.com/javase/tutorial/uiswing/) |          GUI          |
| [Apache Maven Project](https://maven.apache.org/)                      | Dependency management |

# Authors

* [Matteo Rampone](https://github.com/nylone)
* [Alessandro Rossi](https://github.com/AlexRouge)
* [Arianna Salerno](https://github.com/4ri14)

# Special Thanks

* [Gianpaolo Cugola](https://cugola.faculty.polimi.it/home.html)
* [Alberto Archetti](https://github.com/archettialberto)
* [Davide Mazza](https://github.com/davidemazza82)
* [FrankChierici](https://github.com/FrankChierici)
* [KingPowa](https://github.com/KingPowa)

## License

This project is developed in collaboration with [Politecnico di Milano](https://www.polimi.it/) and
[Cranio Creations](https://www.craniocreations.it/)