package hei.school.agriculturalFederation.controller;

import hei.school.agriculturalFederation.model.entities.ActivityMemberAttendance;
import hei.school.agriculturalFederation.model.entities.CollectivityActivity;
import hei.school.agriculturalFederation.model.requests.CreateActivityMemberAttendance;
import hei.school.agriculturalFederation.model.requests.CreateCollectivityActivity;
import hei.school.agriculturalFederation.service.ActivityService;
import hei.school.agriculturalFederation.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class ActivityController {

    private final ActivityService activityService;
    private final AttendanceService attendanceService;

    public ActivityController(ActivityService activityService,
                              AttendanceService attendanceService) {
        this.activityService = activityService;
        this.attendanceService = attendanceService;
    }


    @GetMapping("/{id}/activities")
    @ResponseStatus(HttpStatus.OK)
    public List<CollectivityActivity> getActivities(@PathVariable String id) {
        return activityService.getActivities(id);
    }

    @PostMapping("/{id}/activities")
    @ResponseStatus(HttpStatus.OK)
    public List<CollectivityActivity> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateCollectivityActivity> requests) {
        return activityService.createActivities(id, requests);
    }

    @PostMapping("/{id}/activities/{activityId}/attendance")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ActivityMemberAttendance> createAttendance(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<CreateActivityMemberAttendance> requests) {
        return attendanceService.createAttendance(id, activityId, requests);
    }

    @GetMapping("/{id}/activities/{activityId}/attendance")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityMemberAttendance> getAttendance(
            @PathVariable String id,
            @PathVariable String activityId) {
        return attendanceService.getAttendance(id, activityId);
    }
}