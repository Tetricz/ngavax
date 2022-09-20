# ngavax

Nginx clone made with Java

## Build tools

### Bash scripts for Unix dependencies

* Install Homebrew (Bash) `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
* Unix Dependency script: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/tetricz/ngavax/master/dependency.sh)"`

### Powershell scripts for Windows dependencies

* Install Chocolatey (Powershell):Run `Get-ExecutionPolicy`. If it returns Restricted, then run `Set-ExecutionPolicy AllSigned or Set-ExecutionPolicy Bypass -Scope Process`
* Then run, `Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))`
* Windows Dependency Powershell: `iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/tetricz/ngavax/master/dependency.ps1'))`

## Authors

* Dante
* David Daniels
* Kamden
