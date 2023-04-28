Docker
=================================

Важная хренотень, перед началом настроить надо shell

    🚀 eval $(minikube docker-env)

Создание docker image
---------------------------------

    🚀 docker build -t kubia .
    Sending build context to Docker daemon  4.608kB
    Step 1/3 : FROM node:18
    18: Pulling from library/node
    32fb02163b6b: Pull complete 
    167c7feebee8: Pull complete 
    d6dfff1f6f3d: Pull complete 
    e9cdcd4942eb: Pull complete 
    ca3bce705f6c: Pull complete 
    4f4cf292bc62: Pull complete 
    f95c0e5d7626: Pull complete 
    486cd661187b: Pull complete 
    469c1c051c66: Pull complete 
    Digest: sha256:8d9a875ee427897ef245302e31e2319385b092f1c3368b497e89790f240368f5
    Status: Downloaded newer image for node:18
    ---> 37b4077cbd8a
    Step 2/3 : ADD app.js /app.js
    ---> a4eb40dfdcf5
    Step 3/3 : ENTRYPOINT ["node", "app.js"]
    ---> Running in 5c3b432d3acd
    Removing intermediate container 5c3b432d3acd
    ---> 1b9e258a7db7
    Successfully built 1b9e258a7db7
    Successfully tagged kubia:latest

Запуск
---------------------------

    🚀 docker run --name kubia-c1 -p 8080:8080 -d kubia

Остановка
---------------------

Просмотр
---------------------