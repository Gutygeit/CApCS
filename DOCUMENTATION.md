# Documentation

## User Manual

### Introduction

*CApCS* is a Java Software to synchronize one or more folders, eventually between computers.

### Requirements

To use *CApCS*, you need Java 19, and `CApCS.jar`.

### Using *CApCS*

There are two ways to run *CApCS*:
- In a terminal : run `java -jar CApCS.jar --nogui`.
- In a graphical interface : just run the jar file

Then add one or more folders to synchronize:
- In terminal : type `add folder <path/to/the/folder>`.
- In graphical interface : click **New folder** and enter the path of the folder.

Eventually create a client and a server on two separate computers to synchronize files between them:
- In terminal : type `add client <address> <port>` or `add server <port>`.
- In graphical interface : click **New client** or **New server** and enter address/port.

## Code structure

Packages: `io`, `launcher`, `models`, `ui`.

### `io` package

It contains 2 files: `DirectoryManager.java` and `NetworkManager.java`.
The `DirectoryManager` class contains the code about directory management and the `NetworkManager` class contains the code about the network implementation of the client and server to use *CApCS* between to computers.

### `launcher` package

There are 3 classes in this package which is used to launch our application directly in the terminal or in an UI. The `Launcher` class is the base class for launchers and is used to manage how the program runs. `DaemonLauncher` class is used to create a daemon that allows to run *CApCS* in the terminal. And `UILauncher` class contains the code that initialize the user interface of our application.

### `models` package

The `Document` class represents a synchronized file. Next, `ListenerWithStream` is a helper class to associate a stream to a listener. The `TreeDiff` class represents differences between file trees. And `TreeListener` is the base class for all managers.

### `ui` package

This package contains UI components used by the graphical interface. `ListPanel.java` lists all the active managers, with buttons to add new ones. `DetailsPanel.java` shows the details about the selected manager. And `ListenersCellRenderer.java` renders a cell in the `ListPanel`.
