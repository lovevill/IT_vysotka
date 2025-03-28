package ru.testPr.test.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeamDistributionService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamDistributionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> distributeTeams() {
    	String query = "SELECT total_teams, allocated_teams, unallocated_teams, " +
                "occupied_audiences, assigned_curators, status FROM distribute_teams()";
        
        Map<String, Object> result = jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("totalTeams", rs.getInt("total_teams"));
            row.put("allocatedTeams", rs.getInt("allocated_teams"));
            row.put("unallocatedTeams", rs.getInt("unallocated_teams"));
            row.put("occupiedAudiences", rs.getInt("occupied_audiences"));
            row.put("assignedCurators", rs.getInt("assigned_curators"));
            row.put("status", rs.getString("status"));
            return row;
        });
        return result;
    }
}

