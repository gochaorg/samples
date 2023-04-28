Заставить бегать pod
===================================================

https://stackoverflow.com/questions/59980445/setting-image-pull-policy-using-kubectl

Сгенерировать deployment.yaml 

    kubectl create deployment first-k8-deploy --image="kubia:latest" -o yaml --dry-run > depl1.yaml

Добавить 

    imagePullPolicy: Never

иначе он поломиться искать на docker hub и не найдет

Примерно так

```yaml
spec:
  template:
    spec:
      containers:
      - image: kubia:latest
        name: kubia
        resources: {}
        imagePullPolicy: Never
```

Еще не плохо бы добавить экспортируемые порты

```yaml
spec:
  template:
    spec:
      containers:
        ports:
        - containerPort: 8080
```

Теперь запускаем

    🚀 kubectl apply -f depl1.yaml 

Делаем доступным по IP
======================================

Для minikube магия

    🚀 minikube tunnel

Запуск службы

    🚀 kubectl expose deployment kubia --type=LoadBalancer --name=kubia-svc
    service/kubia-svc exposed

Просмотр служб

    🚀 kubectl get svc
    NAME             TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
    hello-minikube   NodePort       10.96.148.140   <none>        8080:31040/TCP   26h
    kubernetes       ClusterIP      10.96.0.1       <none>        443/TCP          47h
    kubia-svc        LoadBalancer   10.100.66.129   <pending>     8080:30093/TCP   11s

Без магии выше, в случае minikube не дождемся (`<pending>`)

    🚀 kubectl get svc
    NAME             TYPE           CLUSTER-IP      EXTERNAL-IP     PORT(S)          AGE
    hello-minikube   NodePort       10.96.148.140   <none>          8080:31040/TCP   26h
    kubernetes       ClusterIP      10.96.0.1       <none>          443/TCP          47h
    kubia-svc        LoadBalancer   10.100.66.129   10.100.66.129   8080:30093/TCP   7m15s

Дождался

    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-c55gv

Масштабируем в пределах одного сервера (node)
=================================================

    🚀 kubectl scale -n default deployment kubia --replicas=3

Смотрим pods

    🚀 kubectl get pods
    NAME                     READY   STATUS    RESTARTS   AGE
    kubia-5b55b5c484-87xbs   1/1     Running   0          2m55s
    kubia-5b55b5c484-c55gv   1/1     Running   0          40m
    kubia-5b55b5c484-jdjqc   1/1     Running   0          2m55s

Тоже самое, но с учетом имени приложения. 
Имя приложения это метка (label) в спеке.

    🚀 kubectl get pods -l 'app=kubia'
    NAME                     READY   STATUS    RESTARTS   AGE
    kubia-5b55b5c484-87xbs   1/1     Running   0          4m40s
    kubia-5b55b5c484-c55gv   1/1     Running   0          42m
    kubia-5b55b5c484-jdjqc   1/1     Running   0          4m40s


Проверяем

    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-jdjqc
    user|03:46:18|~$
    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-c55gv
    user|03:46:19|~$
    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-jdjqc
    user|03:46:20|~$
    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-c55gv
    user|03:46:21|~$
    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-c55gv
    user|03:46:22|~$
    🚀 curl 10.100.66.129:8080/abc
    You've hit kubia-5b55b5c484-87xbs
