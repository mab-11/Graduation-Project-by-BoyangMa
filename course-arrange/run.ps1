# Run the project
$ErrorActionPreference = "Continue"

$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_192"
$env:MAVEN_HOME = "E:\apache-maven-3.6.3"
$env:PATH = "$env:MAVEN_HOME\bin;$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Java version:"
java -version

Write-Host "Maven version:"
& "$env:MAVEN_HOME\bin\mvn.cmd" --version

Write-Host "Compiling project..."

# Use cmd to change directory
cmd /c "cd /d e:\jz\3500\代码\course-arrange && mvn clean compile"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful! Starting application..."
    cmd /c "cd /d e:\jz\3500\代码\course-arrange && mvn spring-boot:run"
} else {
    Write-Host "Compilation failed!"
    exit 1
}
