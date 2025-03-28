package ru.testPr.test.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Map<String, Object>> getDates() {
//        String query = "SELECT * FROM get_dates()";
//        return jdbcTemplate.query(query, (rs, rowNum) -> {
//            Map<String, Object> row = new HashMap<>();
//            row.put("value", rs.getString("id"));
//            row.put("label", rs.getString("date"));
//            return row;
//        });
//    }

    public List<Map<String, Object>> getSchools() {
        String query = "SELECT * FROM get_schools()";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("value", rs.getString("id"));
            row.put("label", rs.getString("school_name"));
            return row;
        });
    }

    public List<Map<String, Object>> getAudiences() {
        String query = "SELECT * FROM get_audiences()";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("value", rs.getString("audience_number"));
            row.put("label", rs.getString("audience_number"));
            return row;
        });
    }

    public Map<String, List<Map<String, Object>>> getAllFilters() {
        Map<String, List<Map<String, Object>>> filters = new HashMap<>();
//        filters.put("dates", getDates());
        filters.put("schools", getSchools());
        filters.put("audiences", getAudiences());
        return filters;
    }
}

