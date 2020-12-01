package org.example.ha.srvc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Входная точка приложения. <br>
 *
 * Файл/файлы конфигурации являются:
 * <code>application.yml</code> и/или <code>application-<i>имя_профиля</i>.yml</code>  <br>
 *
 * В файлах прописываеться номер порта и прочие настройки
 */
@SpringBootApplication //Указываем что spring boot app
@EnableEurekaClient    //Указываем что является клиентом eureka
public class Service {
    public static void main(String[] args){
        SpringApplication.run(Service.class,args);
    }
}
