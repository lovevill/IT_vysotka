package ru.testPr.test.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.testPr.test.Service.FilterService;

@RestController
@RequestMapping("/api/filters")
public class FilterController {
	
	@Autowired
    private FilterService filterService;
	
	@GetMapping
    public Map<String, List<Map<String, Object>>> getAllFilter(){
    	return filterService.getAllFilters();
    }
}
