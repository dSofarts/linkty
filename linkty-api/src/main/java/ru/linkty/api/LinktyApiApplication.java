package ru.linkty.api;

import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableConfigurationProperties
public class LinktyApiApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    SpringApplication.run(LinktyApiApplication.class, args);
    log.info("""
        
        :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        ::                   LINKTY SERVICE STARTED                    ::
        :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
  }
}
