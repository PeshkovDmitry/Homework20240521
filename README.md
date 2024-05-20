# Задание

Используйте Spring Actuator для отслеживания метрик вашего приложения.
Настройте визуализацию этих метрик с использованием Prometheus и Grafana.

# Примечание

Кастомные метрики реализованы для shop-service (количество доступов к домашней странице и количество покупок).
Папка с настройками application-config должна быть в домашней папке пользователя.
Все настройки микросервисов находятся в ней, в том числе настройки Actuator.  

Рабочая страничка: http://localhost:8080/shop-service 

За основу взята домашняя работа с прошлого семинара.

# Для справки

## Запуск Prometheus в Docker 

sudo docker run -d --name=prometheus -p 9090:9090 prom/prometheus

Далее заходим в контейнер: sudo docker exec -it prometheus /bin/sh

Вручную исправляем конфигурацию: vi /etc/prometheus/prometheus.yml

В последней строке вручную меняем IP адрес своего хоста.

Останавливаем контейнер: sudo docker stop prometheus

Запускаем контейнер: sudo docker start prometheus

## Запуск Grafana в Docker

sudo docker run -d --name=grafana -p 3000:3000 grafana/grafana

Стандартные логин/пароль: admin/admin

При настройке соединения также необходимо использовать фактический IP адрес хоста
