import com.younho.MyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MyServiceTest {

    @Autowired
    private MyService myService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("조건(some_condition)에 맞을 경우 상태가 'processed'로 업데이트 되어야 한다")
    void processData_shouldUpdateStatus_whenConditionMet() {
        // given
        long id = 1L; // data.sql에 정의된 데이터
        String param = "processed";

        // when
        myService.processData(id, param);

        // then
        String updatedStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM MY_TABLE WHERE id = ?", String.class, id);
        assertEquals(param, updatedStatus);
    }

    @Test
    @DisplayName("조건에 맞지 않을 경우 상태가 변경되지 않아야 한다")
    void processData_shouldNotUpdateStatus_whenConditionNotMet() {
        // given
        long id = 2L; // data.sql에 정의된 데이터 (name='other_condition')
        String param = "processed";
        String originalStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM MY_TABLE WHERE id = ?", String.class, id);

        // when
        myService.processData(id, param);

        // then
        String finalStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM MY_TABLE WHERE id = ?", String.class, id);
        assertEquals(originalStatus, finalStatus); // 상태가 'ready'로 유지되어야 함
        assertEquals("ready", finalStatus);
    }
}