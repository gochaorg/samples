package org.example.ha.tezt

import okhttp3.OkHttpClient

/**
 * Клиент http
 */
trait HttpClient {
    /**
     * Клиент http
     */
    private OkHttpClient clientInstace

    /**
     * Возвращает клиента http
     * @return Клиент http
     */
    synchronized OkHttpClient getClient(){
        if( clientInstace!=null )return clientInstace
        clientInstace = new OkHttpClient()
        clientInstace
    }

    /**
     * Указывает клиента http
     * @param client Клиент http
     */
    synchronized void setClient(OkHttpClient client){
        clientInstace = client
    }
}
