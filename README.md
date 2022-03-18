# GS Pro Bridge

Java implementation for communication between launch monitor(s) and the GS Pro software.

> At this point this is just a playground for JavaFX and Spring, playing around with the idea of getting my Tittle X Play working with GS Pro so I can give it a go.

## GS Pro Api

Api is defined at [GSPro Open Connect V1](https://gsprogolf.com/GSProConnectV1.html).  The client provides interfaces and classes to be used within:

- Connector Api Data
- Launch Monitor (Provider)

## GS Pro Client

Client used to connect to and communicate with the [GSPro Open Connect V1](https://gsprogolf.com/GSProConnectV1.html).

> DEPRECATED

At this point the client is just an added layer which isn't needed.  I've left it in there (deprecated) incase it ends up getting used by something else.  But the same functionality has been implemented in the `gspro-app` project using FX properties to allow for status updates.

## GS Pro App

Java FX application (spring boot backed) providing the interface for the GS Pro and Launch Monitor management.  The application is pretty straight forward:

### GS Pro / Launch Monitor

![GS Pro/Launch Monitor](gspro-app/docs/gs-pro-launch-monitor.png)

### Shot History

![Shot History](gspro-app/docs/shot-history.png)

### Debug Log

![Debug Log](gspro-app/docs/debug-log.png)

## Contribution

> Java development in vscode isn't the greatest.  You may need to refresh/restart the Java Language server a couple times to get things going. 

## License

[MIT License](LICENSE.md)
