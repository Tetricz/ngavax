# ngavax

Nginx clone made with Java

## Build tools

### Bash scripts for Unix dependencies

Install Homebrew (Bash)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)" && /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Tetricz/ngavax/main/dev-tools/dependency.sh)"
```

### Powershell scripts for Windows dependencies

Install Chocolatey and download OpenJDK, gradle (Admin Powershell)

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1')); iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/Tetricz/ngavax/main/dev-tools/dependency.ps1'));
```

## Authors

* Dante
* David Daniels
* Kamden Burke
