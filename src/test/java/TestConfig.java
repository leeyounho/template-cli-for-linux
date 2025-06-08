import com.younho.MyRepository;
import com.younho.MyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    /**
     * H2 인메모리 데이터 소스를 생성합니다.
     * 테스트 실행 시 자동으로 classpath에 있는 schema.sql과 data.sql을 실행하여
     * 테이블 생성과 데이터 초기화를 수행합니다.
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2) // H2 데이터베이스 사용
                .addScript("classpath:schema.sql") // 테이블 생성 스크립트
                .addScript("classpath:data.sql")   // 초기 데이터 삽입 스크립트
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MyRepository myRepository() {
        return new MyRepository(jdbcTemplate());
    }

    @Bean
    public MyService myService() {
        return new MyService(myRepository());
    }
}
