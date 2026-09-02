#!/bin/bash

FAILED_SUITE_PATH="target/surefire-reports/testng-failed.xml"

if [ -f "$FAILED_SUITE_PATH" ]; then
    echo "[INFO] Найдены упавшие тесты с предыдущего прогона."
    echo "[INFO] Запуск только упавших кейсов..."
    ./mvnw test -Dsurefire.suiteXmlFiles=$FAILED_SUITE_PATH
else
    echo "[WARNING] Файл $FAILED_SUITE_PATH не найден."
    echo "[WARNING] Возможно, предыдущий прогон завершился без падений или отчеты были очищены."
fi