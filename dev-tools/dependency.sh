#!/bin/bash
echo "Installing OpenJDK 17"
brew install openjdk@17
echo "Installing gradle via Homebrew"
brew install gradle@7

brew cleanup openjdk@17
brew cleanup gradle@7
