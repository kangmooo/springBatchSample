package com.dhi.sample.batch;

import com.dhi.sample.common.entity.PamMapping;
import com.dhi.sample.common.entity.SpringBatchSampleTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.dhi.sample.common.config.Constants.CHUNK_SIZE;
import static com.dhi.sample.common.config.Constants.PAGE_SIZE;

/**
 * prevision batch job step JpaPagingItemReader writer 공통 함수 및  JobLauncher bean 정의
 *
 * @author SungTae, Kang
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChunkJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    String queryStr = new StringBuilder()
                .append(" select *  ")
                .append(" from pam_mapping")
                .append(" where 1=1 ")
                .append("     and active_yn ='Y' ")
                .append("     and used_yn ='Y' ")
                .append("     and model_seq is not null ")
                .toString();
    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    public Job job() {
        return jobBuilderFactory.get("jobName").start(
                stepBuilderFactory.get("jobName" + "Step")
                        .<PamMapping, SpringBatchSampleTest>chunk(CHUNK_SIZE)
                        .reader(reader("readerName", queryStr, PamMapping.class))
                        .processor(p())
                        .writer(w())
                        .taskExecutor(taskExecutor())
                        .throttleLimit(5)
                        .build())
                .build();
    }



    /**
     * 공통으로 사용 하는 JpaPagingItemReaderBuilder build 하는 함수
     *
     * @param readerName
     * @param queryStr native query String
     * @param clazz return Class
     * @param <T> return type
     * @return
     */
    public <T> JpaPagingItemReader reader(final String readerName, final String queryStr, Class clazz) {
        JpaNativeQueryProvider<T> queryProvider = new JpaNativeQueryProvider<T>();// for native query usage
        queryProvider.setEntityClass(clazz);
        queryProvider.setSqlQuery(queryStr);
        JpaPagingItemReader<T> reader = new JpaPagingItemReader<T>() {
            // 완료 된 이후 에  page 를  이동하게 되면 1페이지를 건너 뛰기 때문에 재정의 함
            @Override
            public int getPage() {
                return 0;
            }
        };
        reader.setName(readerName);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(PAGE_SIZE);
        reader.setQueryProvider(queryProvider);
        return reader;
    }



    public ItemProcessor<PamMapping, SpringBatchSampleTest> p() {
        return item -> {
            // do something
            log.debug(item.toString());
            return new SpringBatchSampleTest(item.getPam_mapping_seq(), item.getAsset_seq(), item.getModel_seq(), item.getActive_yn(), item.getUsed_yn(), "thi");
        };
    }

    public  ItemWriter <SpringBatchSampleTest> w() {
        return list -> {
            if (!list.isEmpty()) {
                try(Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(new StringBuilder()
                            .append(" insert into spring_batch_sample_test (pam_mapping_seq, asset_seq, model_seq, active_yn, used_yn ,comments) ")
                            .append(" values (?, ?, ?, ?, ?, ?) ")
                            .toString())) {
                    for(SpringBatchSampleTest springBatchSampleTest : list) {
                        stmt.setLong(1, springBatchSampleTest.getPam_mapping_seq());   // raw_tag_data
                        stmt.setLong(2, springBatchSampleTest.getAsset_seq());   // raw_tag_data
                        stmt.setLong(3, springBatchSampleTest.getModel_seq());   // raw_tag_data
                        stmt.setString(4, springBatchSampleTest.getActive_yn());               // org_raw_tag_data
                        stmt.setString(5, springBatchSampleTest.getUsed_yn());               // org_raw_tag_data
                        stmt.setString(6, springBatchSampleTest.getComments());               // org_raw_tag_data
                        stmt.addBatch();
                    };
                    stmt.executeBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("bulk execute error");
                }
            }
        };
    }
}