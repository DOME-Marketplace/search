package it.eng.dome.search.config;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

    /*
    * THIS CLASS IS IMPORTANT TO ENABLE SCEDULING PROCESS
    * At the time we use Default configuration
    * (the code below is commented but the class is important to enable scheduling)
     */


//    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);
//
//    private final TaskExecutionProperties taskExecutionProperties;
//
//    public AsyncConfiguration(TaskExecutionProperties taskExecutionProperties) {
//        this.taskExecutionProperties = taskExecutionProperties;
//    }
//
//    @Override
//    @Bean(name = "taskExecutor")
//    public Executor getAsyncExecutor() {
//        log.debug("Creating Async Task Executor");
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
//        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
//        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
//        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
//        return new ExceptionHandlingAsyncTaskExecutor(executor);
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
}
