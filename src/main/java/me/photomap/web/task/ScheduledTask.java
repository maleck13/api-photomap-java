package me.photomap.web.task;


public interface ScheduledTask extends Runnable {

  Schedule scheduleEvery();

}
