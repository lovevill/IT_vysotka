package ru.testPr.test.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.testPr.test.Service.TeamDistributionService;

import java.util.Map;

@RestController
@RequestMapping("/api/distribute-teams")
public class TeamDistributionController {

    private final TeamDistributionService teamDistributionService;

    @Autowired
    public TeamDistributionController(TeamDistributionService teamDistributionService) {
        this.teamDistributionService = teamDistributionService;
    }

    @GetMapping("/distribute")
    public Map<String, Object> distributeTeams() {
        // Вызываем метод распределения команд и получаем результат
        return teamDistributionService.distributeTeams();
    }
}

