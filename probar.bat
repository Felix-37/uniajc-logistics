@echo off
rem Compila el codigo y las pruebas, y corre las pruebas con JUnit 5.
rem Usa el JDK de JAVA_HOME si esta definido; si no, el java del PATH.
setlocal enabledelayedexpansion
set "JAVAC=javac"
set "JAVA=java"
if defined JAVA_HOME (
    set "JAVAC=%JAVA_HOME%\bin\javac"
    set "JAVA=%JAVA_HOME%\bin\java"
)
set "JUNIT=lib/junit-platform-console-standalone-1.11.4.jar"

cd /d "%~dp0"
if exist out rmdir /s /q out

rem Lista de fuentes con rutas relativas, por si la carpeta tiene espacios.
call :listar src out-fuentes.txt
call :listar test out-pruebas.txt

"%JAVAC%" -encoding UTF-8 -d out/main @out-fuentes.txt || goto :error
"%JAVAC%" -encoding UTF-8 -cp "out/main;%JUNIT%" -d out/test @out-pruebas.txt || goto :error

del out-fuentes.txt out-pruebas.txt
"%JAVA%" -jar "%JUNIT%" execute --class-path "out/main;out/test" --scan-class-path --details=tree
exit /b %errorlevel%

:listar
set "base=%CD%\"
(for /r %1 %%f in (*.java) do (
    set "f=%%f"
    set "f=!f:%base%=!"
    echo !f:\=/!
)) > %2
exit /b 0

:error
del out-fuentes.txt out-pruebas.txt 2>nul
echo Error de compilacion.
exit /b 1
