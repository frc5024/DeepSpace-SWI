<!-- # DeepSpace-SWI 

Our offseason codebase for DeepSpace was designed from the ground up over the course of four weeks to meet some simple goals:
 - Use Java.
   - A proof of concept for our 2020 language switch.
   - Learning how the language-dependant libraries function in Java instead of C++.
 - Make use of a portable common library to make development easier with helpers and wrappers.
 - Make use of our Subsystem redesign, the `LoopableSubsystem`.
 - Use autonomous field navigation to perfectly score at least one hatch during sandstorm.
 - Use a vision system to align with targets.
 - Climb with the press of a button, autonomously.
 - Execute commands, and interact with CAN devices in an efficient manner to reduce out loop times (currently sitting around 0.000015 seconds per loop)

## Running tests
To run the unit tests, use the following:
```
./gradlew :test
``` -->

# Design

The software that powers HATCHfield (our [2019 Robot](https://frc5024.github.io/webdocs/docs/robots/hatchfield)) was built with the following goals:

 - Intuitive control scheme for drivers
 - Software design oriented around needs and wants
 - Detailed logging system to assist software diagnosis
 - Execute repetitive tasks autonomously

## Needs & Wants

In contrast to our previous robots' code, this codebase implements `LoopableSubsystem`, which is a replacement for [WPIlib's `Subsystem`](https://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/command/Subsystem.html). LoopableSubsystems are registered with their own managed schedular. This design both allows I/O to be buffered, which reduces strain on the robot's internal CAN network, and allows the use of *suggestions* or *desires*.

<!-- ### Desires

Many of our subsystems are fed commands and sensor readings from  -->