#!/bin/bash
echo "Building and downloading with Gradle"
./gradlew build || { echo "Gradle build failed!"; exit 1; }

echo "Starting the app"
java -cp app/build/libs/app.jar atm.app.App || { echo "Failed to start the application!"; exit 1; }