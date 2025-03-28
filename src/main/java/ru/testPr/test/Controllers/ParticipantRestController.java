package ru.testPr.test.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import ru.testPr.test.Service.ParticipantService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participants")
public class ParticipantRestController {

    private final ParticipantService participantService;

    public ParticipantRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchParticipants(@RequestParam String query, @RequestParam(required = false) Integer schoolId, @RequestParam(required = false) Integer audienceNumber) {
        return participantService.searchParticipants(query, schoolId, audienceNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getParticipantById(@PathVariable Long id) {
        try {
        	Map<String, Object> participant = participantService.getParticipantById(id);
            return ResponseEntity.ok(participant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/attendance")
    public ResponseEntity<?> updateAttendance(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean attended = request.get("attended");
        if (attended == null || (attended != false && attended != true)) {
            return ResponseEntity.badRequest().body("Invalid attended value");
        }

        try {
            participantService.updateAttendance(id, attended);
            return ResponseEntity.ok("Attendance updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found");
        }
    }
}

