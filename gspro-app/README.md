# GS Pro Desktop

JavaFX desktop application used to control and monitor the GS Pro Connect Bridge

## Configuration

Thanks to a bunch of people smarter than I, when developing the Desktop application you'll need to connect to [http://localhost:6080](http://localhost:6080) which will open the FluxBox desktop.  From there you can run the application in VS Code devcontainer and it will start within the FlexBox desktop.

> Alternatively you can connect to vnc using port 5901

For more details on this [https://lucasjellema.medium.com/run-and-access-gui-inside-vs-code-devcontainers-b572643d0d2a](https://lucasjellema.medium.com/run-and-access-gui-inside-vs-code-devcontainers-b572643d0d2a).

### devcontainer.json

The applicable `devcontainer.json` lines are:

```

	"features": {
		// Install desktop-lite (Fluxbox) on devcontainer
		// When starting GsProConnectApplicationBoot application loads into desktop
		// https://lucasjellema.medium.com/run-and-access-gui-inside-vs-code-devcontainers-b572643d0d2a		
		"desktop-lite": {
			"password": "vscode",
			"webPort": 6080,
			"vncPort": 5901
		}
	},

	// Use 'forwardPorts' to make a list of ports inside the container available locally.
	"forwardPorts": [
		6080,	// desktop-lite web
		5901,	// desktop-lite vnc
		8080	// spring-boot
	],	

```