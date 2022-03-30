# Garmin Launch Monitors

Provides implementations of the `LaunchMonitor` and `LaunchMonitorProvider` for Garmin products.  

This launch monitor plugin can be installed by downloading and copying the `jar` file into the application folder.

## R10 Launch Monitor

The Java implementation of [https://github.com/travislang/gspro-garmin-connect-v2](https://github.com/travislang/gspro-garmin-connect-v2).

The `R10LaunchMonitor` opens a connection to which the R10 application connects.  It then listens to appropriate messages and forwards shot requests to the `LaunchMonitorListener`.  It looks like:

- the monitor accepts ball and club data in separate messages, then receives a shot request that finally sends that information.
- the monitor also continually pings and handles challenges from the R10


