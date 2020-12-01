package org.example.ha.tezt

import groovy.json.JsonSlurper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Клиент eureka
 */
class EurekaApi implements HttpClient {
    String api = "http://localhost:28000/eureka/apps"

    /**
     * Получение списка сервисов
     * @return список сервисов
     */
    List<EurekaApp> getApps(){
        Request req = new Request.Builder()
            .url("http://localhost:28000/eureka/apps")
            .header("Accept","application/json")
            .build()

        def result = []

        try(
            Response response = client.newCall(req).execute()
        ) {
            String body = response.body().string();
            def json = new JsonSlurper().parseText(body)

            json.applications.application.each { jsn ->
                result.add( new EurekaApp(jsn) )
            }
        }

        result
    }
}
