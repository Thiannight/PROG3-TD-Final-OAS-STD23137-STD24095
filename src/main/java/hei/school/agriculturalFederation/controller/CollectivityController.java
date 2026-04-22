package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.AssignCollectivityIdentity;
import hei.school.agriculturalFederation.model.Collectivity;
import hei.school.agriculturalFederation.model.CreateCollectivity;
import hei.school.agriculturalFederation.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Collectivity> createCollectivities(@RequestBody List<CreateCollectivity> collectivities) {
        return collectivityService.createCollectivities(collectivities);
    }

    @PatchMapping("/{collectivityId}/identity")
    @ResponseStatus(HttpStatus.OK)
    public Collectivity assignIdentity(
            @PathVariable String collectivityId,
            @RequestBody AssignCollectivityIdentity request) {
        return collectivityService.assignIdentity(collectivityId, request);
    }
}