Практика kubernates
=========================

Пробуем по книге Kubernates in action

- [basic-app - простой веб сервер](basic-app/readme.md)
- [Создание docker image](docker.md)
- [kuber-1 / Базовые операции в пределах одного сервера](kuber-1.md)
  - Заставить бегать pod
  - Делаем доступным по IP
  - Масштабируем в пределах одного сервера (node)
- [replication controller](kuber-1mNw.md)
- [Кластерное приложение](kuber-2.md)

Подготовка
======================

Важная хренотень, перед началом настроить надо shell

    🚀 eval $(minikube docker-env)

Устанавливает переменные окружения

    declare -x DOCKER_CERT_PATH="/home/user/.minikube/certs"
    declare -x DOCKER_HOST="tcp://192.168.49.2:2376"
    declare -x DOCKER_TLS_VERIFY="1"
    declare -x MINIKUBE_ACTIVE_DOCKERD="minikube"
