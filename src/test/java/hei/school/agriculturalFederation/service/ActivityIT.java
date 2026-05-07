package hei.school.agriculturalFederation.service;

import hei.school.agriculturalFederation.api.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityIT {

    final ApiClient apiClient = new ApiClient();

    @Test
    @Order(1)
    void get_activities_collectivity_not_found() {
        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.get("/collectivities/col-99/activities",
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}));
        assertTrue(exception.getMessage().contains("HTTP Error: 404"));
        log.info(exception.getMessage());
    }

    @Test
    @Order(2)
    void create_activity_with_recurrence_rule_ok() {
        var body = List.of(Map.of(
                "label", "Assemblée générale mensuelle",
                "activityType", "MEETING",
                "memberOccupationConcerned", List.of(),
                "recurrenceRule", Map.of("weekOrdinal", 2, "dayOfWeek", "SU")
        ));

        var result = apiClient.post(
                "/collectivities/col-1/activities",
                body,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info("Created activity (recurrence): " + result);

        var activity = result.get(0);
        assertNotNull(activity.get("id"));
        assertEquals("Assemblée générale mensuelle", activity.get("label"));
        assertEquals("MEETING", activity.get("activityType"));
        assertNull(activity.get("executiveDate"));
        assertNotNull(activity.get("recurrenceRule"));
    }

    @Test
    @Order(3)
    void create_activity_with_executive_date_ok() {
        var body = List.of(Map.of(
                "label", "Formation juniors",
                "activityType", "TRAINING",
                "memberOccupationConcerned", List.of("JUNIOR"),
                "executiveDate", "2026-06-15"
        ));

        var result = apiClient.post(
                "/collectivities/col-1/activities",
                body,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        assertNotNull(result);
        assertFalse(result.isEmpty());
        log.info("Created activity (executive date): " + result);

        var activity = result.get(0);
        assertEquals("TRAINING", activity.get("activityType"));
        assertEquals("2026-06-15", activity.get("executiveDate"));
        assertNull(activity.get("recurrenceRule"));
    }

    @Test
    @Order(4)
    void create_activity_both_recurrence_and_date_ko() {
        var body = List.of(Map.of(
                "label", "Activité invalide",
                "activityType", "OTHER",
                "recurrenceRule", Map.of("weekOrdinal", 1, "dayOfWeek", "MO"),
                "executiveDate", "2026-06-15"
        ));

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/col-1/activities",
                        body,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}));

        assertTrue(exception.getMessage().contains("HTTP Error: 400"));
        log.info(exception.getMessage());
    }

    @Test
    @Order(5)
    void get_activities_ok() {
        var result = apiClient.get(
                "/collectivities/col-1/activities",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        assertNotNull(result);
        log.info("Activities for col-1: " + result);
        assertTrue(result.size() >= 2);
    }

    @Test
    @Order(6)
    void get_attendance_activity_not_found() {
        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.get(
                        "/collectivities/col-1/activities/unknown-id/attendance",
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}));
        assertTrue(exception.getMessage().contains("HTTP Error: 404"));
        log.info(exception.getMessage());
    }

    @Test
    @Order(7)
    void create_and_get_attendance_ok() {
        var activities = apiClient.get(
                "/collectivities/col-1/activities",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        assertFalse(activities.isEmpty());

        String activityId = activities.stream()
                .filter(a -> "MEETING".equals(a.get("activityType")))
                .findFirst()
                .map(a -> (String) a.get("id"))
                .orElseThrow(() -> new AssertionError("No MEETING activity found"));

        log.info("Testing attendance for activityId=" + activityId);

        var body = List.of(
                Map.of("memberIdentifier", "C1-M1", "attendanceStatus", "ATTENDED"),
                Map.of("memberIdentifier", "C1-M2", "attendanceStatus", "MISSING")
        );

        var created = apiClient.post(
                "/collectivities/col-1/activities/" + activityId + "/attendance",
                body,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        assertNotNull(created);
        assertEquals(2, created.size());
        log.info("Created attendance: " + created);

        var all = apiClient.get(
                "/collectivities/col-1/activities/" + activityId + "/attendance",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        assertNotNull(all);
        log.info("All attendance: " + all);

        var m1 = all.stream()
                .filter(a -> {
                    @SuppressWarnings("unchecked")
                    var desc = (Map<String, Object>) a.get("memberDescription");
                    return "C1-M1".equals(desc != null ? desc.get("id") : null);
                }).findFirst();
        m1.ifPresent(a -> assertEquals("ATTENDED", a.get("attendanceStatus")));

        var m2 = all.stream()
                .filter(a -> {
                    @SuppressWarnings("unchecked")
                    var desc = (Map<String, Object>) a.get("memberDescription");
                    return "C1-M2".equals(desc != null ? desc.get("id") : null);
                }).findFirst();
        m2.ifPresent(a -> assertEquals("MISSING", a.get("attendanceStatus")));
    }

    @Test
    @Order(8)
    void cannot_update_confirmed_attendance() {
        var activities = apiClient.get(
                "/collectivities/col-1/activities",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        String activityId = activities.stream()
                .filter(a -> "MEETING".equals(a.get("activityType")))
                .findFirst()
                .map(a -> (String) a.get("id"))
                .orElseThrow(() -> new AssertionError("No MEETING activity found"));

        var body = List.of(
                Map.of("memberIdentifier", "C1-M1", "attendanceStatus", "MISSING")
        );

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/col-1/activities/" + activityId + "/attendance",
                        body,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}));

        assertTrue(exception.getMessage().contains("HTTP Error: 400"));
        log.info("Correctly rejected: " + exception.getMessage());
    }

    @Test
    @Order(9)
    void attendance_for_training_junior_only() {
        var activities = apiClient.get(
                "/collectivities/col-1/activities",
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        String activityId = activities.stream()
                .filter(a -> "TRAINING".equals(a.get("activityType")))
                .findFirst()
                .map(a -> (String) a.get("id"))
                .orElseThrow(() -> new AssertionError("No TRAINING activity found"));

        var bodyOptional = List.of(
                Map.of("memberIdentifier", "C1-M1", "attendanceStatus", "ATTENDED")
        );
        var resultOptional = apiClient.post(
                "/collectivities/col-1/activities/" + activityId + "/attendance",
                bodyOptional,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        assertNotNull(resultOptional);
        log.info("Optional attendance (PRESIDENT -> ATTENDED): " + resultOptional);

        var bodyInvalid = List.of(
                Map.of("memberIdentifier", "C1-M1", "attendanceStatus", "MISSING")
        );
        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/col-1/activities/" + activityId + "/attendance",
                        bodyInvalid,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}));
        assertTrue(exception.getMessage().contains("HTTP Error: 400"));
        log.info("Correctly rejected MISSING for non-concerned member: " + exception.getMessage());
    }
}