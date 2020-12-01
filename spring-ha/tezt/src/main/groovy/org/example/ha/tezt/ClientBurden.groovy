package org.example.ha.tezt


import okhttp3.Request
import okhttp3.Response
import org.example.stat.SequenceNumSamples

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

import org.example.okhttp.HttpClient

/**
 * Нагрузка одного клиента
 */
//@TypeChecked
class ClientBurden implements HttpClient {
    /**
     * Время начала (System.currentTimeMillis())
     */
    volatile long started;

    /**
     * Время завершения (System.currentTimeMillis())
     */
    volatile long finished;

    /**
     * Максимальная продолжительность (мс) нагрузки
     */
    volatile long maxDuration = 1000L * 60L

    /**
     * Минимальное время (мс) паузы между запросами
     */
    volatile long sleepMin = 500;

    /**
     * Максимальное время (мс) паузы между запросами
     */
    volatile long sleepMax = 1500;

    final ThreadLocalRandom random = ThreadLocalRandom.current()

    /**
     * Состояние процесса
     */
    public static enum State {
        /** Подготовлено, но не запущено */
        Prepare,

        /** Запущен процесс */
        Started,

        /** Завершено */
        Finished,

        /** Пауза между запросами */
        Sleep,

        /** Посылка запроса */
        Request,

        /** собр статистики запроса */
        Collect,
    }
    final Map<State, SequenceNumSamples> statesDuration = new ConcurrentHashMap<>()

    /**
     * Получение списка запросов на момент времени runTime от начала (started)
     * @param runTime время (мс) прошедшее с начала старта
     * @return Запросы
     */
    protected List<Request> clientRequestsAtTime( long runTime ){
        ArrayList<Request> requests = new ArrayList<>()
        return requests
    }

    protected volatile State stateValue = State.Prepare
    protected volatile long lastStateChanged = 0

    /**
     * Возвращает текущее состояние
     * @return текущее состояние
     */
    State getState(){
        stateValue
    }

    /**
     * Указывает текущее состояние
     * @param st текущее состояние
     */
    void setState(State st){
        if( st==null )throw new IllegalArgumentException("st==null");
        synchronized (this){
            long now = System.currentTimeMillis()
            long prevStateStarted = lastStateChanged
            lastStateChanged = now
            if( prevStateStarted>1000L ){
                long stateDuration = now - prevStateStarted
                State state = this.@stateValue
                collectStateDuration(state, stateDuration)
            }
            this.@stateValue = st
        }
    }

    /**
     * Сбор статистики состояний
     * @param st состояние
     * @param durationMSec продолжительность
     */
    void collectStateDuration( State st, long durationMSec ){
        if( st!=null && durationMSec>=0 ){
            statesDuration.
                computeIfAbsent(st, k->new SequenceNumSamples()).add( durationMSec )
        }
    }

    /**
     * Код выполняемый в потоке
     */
    final Runnable runnable = new Runnable() {
        @Override
        void run() {
            started = System.currentTimeMillis()
            lastStateChanged = started
            state = State.Started
            while (true){
                long runTime = System.currentTimeMillis() - started

                if( runTime>maxDuration ){
                    break
                }

                if( Thread.currentThread().isInterrupted() ){
                    break
                }

                clientRequestsAtTime(runTime).each { req ->
                    try {
                        long reqStartedNano = System.nanoTime()
                        state = State.Request
                        try (Response respone = client.newCall(req).execute()) {
                            state = State.Collect
                            long reqFinishedNano = System.nanoTime()
                            collect(req, respone, reqFinishedNano-reqStartedNano )
                        }
                    } finally {
                        state = State.Started
                    }
                }

                long slpMin = Math.min(sleepMin, sleepMax)
                long slpMax = Math.max(sleepMin, sleepMax)
                if( slpMax>0 ){
                    long sleepDuration = slpMax
                    if( slpMax-slpMin>0 ){
                        sleepDuration = random.nextLong(slpMax - slpMin) + slpMin
                    }

                    try {
                        state = State.Sleep
                        //noinspection GroovyUnusedCatchParameter
                        try {
                            Thread.sleep(sleepDuration)
                        } catch (InterruptedException ex1) {
                            break
                        }
                    } finally {
                        state = State.Started
                    }
                }
            }
            finished = System.currentTimeMillis()
            state = State.Finished
        }
    }

    protected void collect( Request request, Response response, long executionNano ){
    }
}
