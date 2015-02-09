package me.photomap.web.me.photomap.web.task;

import me.photomap.web.data.repo.QueueRepo;
import me.photomap.web.data.repo.model.Queue;
import me.photomap.web.service.AmqpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class QueueCleaner implements ScheduledTask{

    private @Autowired QueueRepo queueRepo;
    private @Autowired AmqpService amqpService;

    Logger log = LoggerFactory.getLogger(QueueCleaner.class);

    @Override
    public void run() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -1);
        List<Queue> deleted = queueRepo.deleteQueueByCreatedLessThan(c.getTime());
        log.info("deleted queues " + deleted.size());
        for(Queue q : deleted){
            amqpService.removeQueue(q.getQueueId());
        }

    }

    @Override
    public Schedule scheduleEvery() {
        return new Schedule(TimeUnit.MINUTES, 10);
    }
}
