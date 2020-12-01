package org.example.ha.tezt

import groovy.transform.TypeChecked
import okhttp3.Request
import okhttp3.Response

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

/**
 * Нагрузка одного клиента
 */
//@TypeChecked
class ClientBurden implements HttpClient {
    public static enum State {
        Prepare,
        Started,
        Finished,
        Sleep,
        Request,
        Collect,
    }

    volatile long started;
    volatile long finished;
    volatile long maxDuration = 1000L * 60L
    volatile long sleepMin = 500;
    volatile long sleepMax = 1500;

    final ThreadLocalRandom random = ThreadLocalRandom.current()
    final Map<State,SequenceNumSamples> statesDuration = new ConcurrentHashMap<>()

    protected List<Request> clientRequestsAtTime( long runTime ){
        ArrayList<Request> requests = new ArrayList<>()
        return requests
    }

    protected volatile State stateValue = State.Prepare
    protected volatile long lastStateChanged = 0
    State getState(){
        stateValue
    }
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

    void collectStateDuration( State st, long durationMSec ){
        if( st!=null && durationMSec>=0 ){
            statesDuration.
                computeIfAbsent(st, k->new SequenceNumSamples()).add( durationMSec )
        }
    }

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
