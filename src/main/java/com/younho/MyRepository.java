package com.younho;

import org.springframework.jdbc.core.JdbcTemplate;

public class MyRepository {
    private final JdbcTemplate jdbcTemplate;

    public MyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String findDataById(Long id) {
        return jdbcTemplate.queryForObject("SELECT name FROM my_table WHERE id = ?", String.class, id);
    }

    public void updateStatus(Long id, String status) {
        jdbcTemplate.update("UPDATE my_table SET status = ? WHERE id = ?", status, id);
    }
}
