#!/bin/bash
echo "Installing OpenJDK 18"
brew install openjdk@18
echo "Installing gradle via Homebrew"
brew install gradle@7

brew cleanup openjdk@18
brew cleanup gradle@7
