# ngavax

Nginx clone made with Java

## Build tools

### Bash scripts for Unix dependencies

Install Homebrew (Bash)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Unix Dependency script

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/tetricz/ngavax/master/dependency.sh)"
```

### Powershell scripts for Windows dependencies

Install Chocolatey (Powershell)

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
```

Windows Dependency Powershell:

```powershell
iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/tetricz/ngavax/master/dependency.ps1'))
```

## Authors

* Dante
* David Daniels
* Kamden
