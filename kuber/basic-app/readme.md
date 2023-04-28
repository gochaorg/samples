Простой web-server
========================================

Запускается на 8080, принимает любой запрос выводит информацию о текущем хосте на котором бегает сервер

    🚀 curl -v http://localhost:8080/abc
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > GET /abc HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    > 
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 200 OK
    < Date: Sat, 18 Mar 2023 22:25:26 GMT
    < Connection: keep-alive
    < Keep-Alive: timeout=5
    < Transfer-Encoding: chunked
    < 
    You've hit user-ThinkPad-T15-Gen-2i
    * Connection #0 to host localhost left intact

В лог пишет инфу откуда пришел запрос

    🚀 node app.js 
    sample app starting...
    Recived request from ::ffff:127.0.0.1

