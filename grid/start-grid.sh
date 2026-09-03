#!/bin/bash

echo "Запуск Selenium Grid Hub..."
java -jar selenium-server.jar hub &
HUB_PID=$!

sleep 5

echo "Запуск Selenium Grid Node..."
java -jar selenium-server.jar node --max-sessions 3
NODE_PID=$!

echo "Selenium Grid запущен!"
echo "Для остановки Grid выполните команду: kill $HUB_PID $NODE_PID"
