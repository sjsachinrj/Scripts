@echo off
setlocal

:: Define the list of Git repositories you want to clone
set repositories=(
    https://github.com/repo1.git
    https://github.com/repo2.git
    https://github.com/repo3.git
)

:: Set the directory where you want to clone the repositories
set clone_dir=C:\path\to\your\directory

:: Loop through the list of repositories and clone each one
for %%repo in (%repositories%) do (
    cd %clone_dir%
    git clone %%repo
    if errorlevel 1 (
        echo Failed to clone %%repo
    ) else (
        echo Cloned %%repo successfully
        cd "%%~nxi"
        mvn clean install
    )
)

endlocal
