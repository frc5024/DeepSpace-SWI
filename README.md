# DeepSpace-SWI 

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
```