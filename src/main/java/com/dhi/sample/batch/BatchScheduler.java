package com.dhi.sample.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Date;

/**
 * Prevision batch scheduler
 *
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchScheduler implements SchedulingConfigurer {

    private final JobLauncher jobLauncher;

    private final TaskletJob taskletJob;
    private final ChunkJob chunkJob;

    private final int intervalSeconds = 1;
    private final int intervalTime = intervalSeconds * 1000;

//    private final int POOL_SIZE = 6;
    private final int POOL_SIZE = 1;

    /**
     * scheduler 를 실행 하는 threadPool 정의
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("SchedulerPool-");
        threadPoolTaskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Scheduled(fixedDelay = intervalTime)
    private void executeTasklet() {
        run(taskletJob.job());
    }

    @Scheduled(fixedDelay = intervalTime)
    private void executeChunk() {
        run(chunkJob.job());
    }


    /**
     * batch job execute (MDC.put 으로 로그 추가)
     *
     * @param job
     */
    private void run(Job job) {
        String jobName = job.getName();
        try {
            MDC.put("logFileName", jobName);    // log file name 설정
            jobLauncher.run(job,
                    new JobParametersBuilder()
                            .addString("date", new Date().toString())
                            .addString("jobName", jobName)
                            .toJobParameters());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}