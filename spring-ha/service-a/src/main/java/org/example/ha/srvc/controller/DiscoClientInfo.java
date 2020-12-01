package org.example.ha.srvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Образец Rest контроллера (controllers api endpoint) <br>
 * Информация о доступных сервисах.
 */
@RestController

// Указывает базовый адрес по кторому REST данный контроллер будет доступен
@RequestMapping("/disco")
public class DiscoClientInfo {
    // Данное поле будет автоматом инициализированно spring-ом
    @Autowired
    private DiscoveryClient client;

    /**
     * Получение списка сервисов
     * @return Некий объект, который будет автоматически преобразован в JSON формат
     */
    @RequestMapping(
        value = "/service", // данный метод будет доступен по адресу http://адрес:порт/disco/hib/service
        method = RequestMethod.GET, //для получения данных надо использовать http метод - GET
        //consumes = MediaType.APPLICATION_JSON_VALUE, // Входищий тип данных - JSON
        produces = MediaType.APPLICATION_JSON_VALUE  // Исходящий тип данных - JSON
    )
    public Object services(){
        DiscoveryClient client = this.client;
        if( client==null )throw new IllegalStateException("DiscoveryClient n/a");
        return client.getServices();
    }

    /**
     * Получение информации по конкретному сервису
     * @param name имя сервиса
     * @return информация о экземплярах сервиса
     */
    @RequestMapping("/service/{name}")
    public Object service(@PathVariable("name") String name){
        DiscoveryClient client = this.client;
        if( client==null )throw new IllegalStateException("DiscoveryClient n/a");

        return client.getInstances(name);
    }
}
