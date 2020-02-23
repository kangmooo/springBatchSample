package com.dhi.sample.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TaskletJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    public Job job() {
        return jobBuilderFactory.get("jobName").start(
                stepBuilderFactory.get("jobName" + "Step")
                        .tasklet(
                                (contribution, chunkContext) -> {
                                    // do something
                                    log.info("taskLet 입니다{}", "~~~");
                                    return RepeatStatus.FINISHED;
                                }
                        )
                        .build())
                .build();
    }

}