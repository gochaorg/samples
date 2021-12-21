########################################################################################################################
# id: create
# create container / создание контейнера
docker run --name ${docker.container} -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=${mssql.sa.password}" ${docker.ports} -d ${docker.image}

########################################################################################################################
# id: list
# list containers / получение списка контейнеров
# docker ps -a --format "table {{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
docker ps -a --format "id:{{.ID}}\n name:{{.Names}}\n status:{{.Status}}\n port:{{.Ports}}"

########################################################################################################################
# id: stop
# stop container / остановка контейнера
docker stop ${docker.container}

########################################################################################################################
# id: start
# stop container / получение списка контейнеров
docker start ${docker.container}

########################################################################################################################
# id: delete
# delete container / удаление контейнера
docker rm ${docker.container}