@echo off
echo Starting Selenium Grid...

echo Запуск Selenium Grid Hub...
start "Selenium Hub" java -jar selenium-server.jar hub

timeout /t 5 >nul

echo Запуск Selenium Grid Node...
start "Selenium Node" java -jar selenium-server.jar node --max-sessions 3

echo Selenium Grid запущен!