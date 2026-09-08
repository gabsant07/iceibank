@echo off
setlocal
set "BASE_DIR=%~dp0"
set "MAVEN_VERSION=3.9.11"
set "MAVEN_HOME=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%"
set "ARCHIVE=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%-bin.zip"
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$u='https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip'; Invoke-WebRequest -Uri $u -OutFile '%ARCHIVE%'; Expand-Archive -Path '%ARCHIVE%' -DestinationPath '%BASE_DIR%.mvn' -Force"
  if errorlevel 1 exit /b 1
)
call "%MAVEN_HOME%\bin\mvn.cmd" %*
endlocal
