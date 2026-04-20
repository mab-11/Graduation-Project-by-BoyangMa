@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_192"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Using Java: %JAVA_HOME%
call mvnw.cmd clean compile
