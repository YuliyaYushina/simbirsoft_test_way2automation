@echo off
chcp 65001 > nul
set FAILED_SUITE_PATH=target/surefire-reports/testng-failed.xml

if exist %FAILED_SUITE_PATH% (
    echo [INFO] Найдены упавшие тесты с предыдущего прогона.
    echo [INFO] Запуск только упавших кейсов...
    call mvnw test -Dsurefire.suiteXmlFiles=%FAILED_SUITE_PATH%
) else (
    echo [WARNING] Файл %FAILED_SUITE_PATH% не найден.
    echo [WARNING] Возможно, предыдущий прогон завершился без падений или отчеты были очищены.
)
pause