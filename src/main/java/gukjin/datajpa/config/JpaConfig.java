package gukjin.datajpa.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "gukjin.datajpa",
        repositoryImplementationPostfix = "Impl"
)
public class JpaConfig {
}
