package me.photomap.web.me.photomap.web.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class TaskFactory implements ApplicationContextAware{

    ApplicationContext applicationContext;
    ScheduledExecutorService ex = Executors.newScheduledThreadPool(10);
    private static Set<Integer> registered = new HashSet<>();
    Logger log = LoggerFactory.getLogger(TaskFactory.class);


    @PostConstruct
    private void  init(){
        //starting tasks
        for(Integer i : registered){
            log.info("registering tasks ");
            ScheduledTask t = get(i);
            ex.scheduleAtFixedRate(t,0l,t.scheduleEvery().interval(),t.scheduleEvery().unit());
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static final int QUEUE_CLEANER =0;


    public ScheduledTask get(int taskType){
        ScheduledTask r = null;
        switch (taskType){
            case QUEUE_CLEANER:
                r = applicationContext.getBean(QueueCleaner.class);
                break;
            default:
                throw new UnknownConstantException("factory method unknown constant passed " + String.valueOf(taskType));
        }
        return r;

    }

    public static void registerTask(int task){
        registered.add(task);
    }





    private class UnknownConstantException extends RuntimeException{
        public UnknownConstantException() {
        }

        public UnknownConstantException(String message) {
            super(message);
        }

        public UnknownConstantException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @PreDestroy
    private void tearDown(){
        if(null != ex){
            ex.shutdown();
        }
    }
}
