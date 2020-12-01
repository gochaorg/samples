package org.example.okhttp

import okhttp3.OkHttpClient

/**
 * Клиент http
 */
public trait HttpClient {
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
