package me.photomap.web.task;

import java.util.concurrent.TimeUnit;


public class Schedule {

  private TimeUnit unit;
  private long interval;

  public Schedule(TimeUnit unit, long interval) {
    this.unit = unit;
    this.interval = interval;
  }

  TimeUnit unit() {
    return this.unit;
  }

  long interval() {
    return this.interval;
  }
}
