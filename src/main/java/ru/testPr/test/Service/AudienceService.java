package ru.testPr.test.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AudienceService {
	
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AudienceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addAudienceAndComputers(int audienceNumber, int computerCount, Integer prioritet) {
    	String query = String.format("call add_new_audience(%d, %d, %d)", audienceNumber, computerCount, prioritet);
        jdbcTemplate.execute(query);
    }
    
    public void changeAudienceAndComputers(int audienceNumber, Integer computerCount, Integer prioritet) {
    	String query = String.format("call change_audience(%d, %d, %d)", audienceNumber, computerCount, prioritet);
        jdbcTemplate.execute(query);
    }
    
    public List<Map<String, Object>> getAllAudiences(){
    	String queryDb = String.format("SELECT * FROM get_audiences()");
        
        List<Map<String, Object>> result = jdbcTemplate.query(queryDb, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("audienceNumber", rs.getInt("audience_number"));
            return row;
        });
        return result;
    }
}



