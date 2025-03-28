package ru.testPr.test.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParticipantService {
	
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ParticipantService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> searchParticipants(String query, Integer schoolId, Integer audienceNumber) {
    	String queryDb = String.format("SELECT * FROM find_participant_by_query('%s', %d, %d)", query, schoolId, audienceNumber);
        
        List<Map<String, Object>> result = jdbcTemplate.query(queryDb, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("name", rs.getString("name"));
            row.put("lastName", rs.getString("last_name"));
            row.put("middleName", rs.getString("middle_name"));
            row.put("teamName", rs.getString("team_name"));
            row.put("schoolName", rs.getString("school_name"));
            return row;
        });
        return result;
    }
  
    public Map<String, Object> getParticipantById(Long id) {
    	String queryDb = String.format("SELECT * FROM find_participant_by_id('%d')", id);
        
        List<Map<String, Object>> result = jdbcTemplate.query(queryDb, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("name", rs.getString("name"));
            row.put("lastName", rs.getString("last_name"));
            row.put("middleName", rs.getString("middle_name"));
            row.put("teamName", rs.getString("team_name"));
            row.put("schoolName", rs.getString("school_name"));
            row.put("phoneNumber", rs.getString("phone_number"));
            row.put("email", rs.getString("email"));
            row.put("schoolClass", rs.getInt("school_class"));
            row.put("audience", rs.getInt("audience"));
            row.put("computerNumber", rs.getInt("computer_number"));
            row.put("attended", rs.getBoolean("attended"));
            return row;
        });

        if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new RuntimeException("Participant not found with id: " + id);
        }
    }
    
    public void updateAttendance(Long id, boolean attended) {
    	String query = String.format("call set_attended_by_id(%d, %b)", id, attended);
        jdbcTemplate.execute(query);
    }
    
    public List<Map<String, Object>> getOfflineParticipants() {
    	String queryDb = String.format("SELECT * FROM get_offline_participants()");
        
    	List<Map<String, Object>> result = jdbcTemplate.query(queryDb, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getInt("id"));
            row.put("name", rs.getString("name"));
            row.put("lastName", rs.getString("last_name"));
            row.put("middleName", rs.getString("middle_name"));
            row.put("teamName", rs.getString("team_name"));
            row.put("email", rs.getString("email"));
            row.put("audience", rs.getInt("audience"));
            row.put("computerNumber", rs.getInt("computer_number"));
            return row;
        });
    	
    	return result;
    }
}


