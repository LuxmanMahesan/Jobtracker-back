package com.jobtracker.jobtrackerbackend.controller;

import com.jobtracker.jobtrackerbackend.dto.AnalyseOffreRequest;
import com.jobtracker.jobtrackerbackend.dto.AnalyseOffreResponse;
import com.jobtracker.jobtrackerbackend.service.OffreIAService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analyse")
@CrossOrigin(origins = "*")
public class AnalyseOffreIAController {

    private final OffreIAService service;

    public AnalyseOffreIAController(OffreIAService service) {
        this.service = service;
    }

    @PostMapping("/ia")
    public AnalyseOffreResponse analyser(@RequestBody AnalyseOffreRequest req) throws Exception {
        if (req.url == null || req.url.isBlank()) {
            throw new IllegalArgumentException("URL manquante");
        }
        return service.analyser(req.url);
    }
}
