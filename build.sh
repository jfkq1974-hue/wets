#!/bin/bash

# Create necessary directories
mkdir -p build/classes
mkdir -p build/plugin

# Compile Java files
echo "Compiling Java files..."
javac -cp "." -d build/classes src/main/java/com/augmentedvoid/*.java

# Copy resources
echo "Copying resources..."
cp -r src/main/resources/* build/plugin/

# Create plugin.yml in the right location
cp plugin.yml build/plugin/

# Create JAR file (simplified approach)
echo "Creating JAR file..."
cd build/classes
jar cf ../AugmentedVoid.jar com/
cd ../..
cp build/classes/../AugmentedVoid.jar ./

echo "Build complete! AugmentedVoid.jar created."