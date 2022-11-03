package gukjin.datajpa.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaRepositories(
        basePackages = "gukjin.datajpa",
        repositoryImplementationPostfix = "Impl"
)
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
        return ()-> Optional.of(UUID.randomUUID().toString());
    }
}
