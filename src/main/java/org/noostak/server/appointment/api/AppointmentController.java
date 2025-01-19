package org.noostak.server.appointment.api;

import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.application.AppointmentService;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.request.AvailableTimesRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.global.success.core.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.server.appointment.common.AppointmentSuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/{groupId}/appointments")
    public ResponseEntity<SuccessResponse<AppointmentCreateResponse>> createAppointment(
//            @AuthenticationPrincipal Long memberId,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody AppointmentCreateRequest request
    ) {
        Long memberId = 1L;
        AppointmentCreateResponse appointmentCreateResponse = appointmentService.createAppointment(memberId, groupId, request);
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_CREATE_APPOINTMENT, appointmentCreateResponse));
    }

    @PostMapping("/{groupId}/appointments/{appointmentId}/timetable")
    public ResponseEntity<SuccessResponse<Void>> saveAvailableTimeSelection(
//            @AuthenticationPrincipal Long memberId
            @PathVariable(name = "groupId") Long groupId,
            @PathVariable(name = "appointmentId") Long appointmentId,
            @RequestBody AvailableTimesRequest request
    ) {
        Long memberId = 2L;
        appointmentService.saveAvailableTimes(memberId, groupId, appointmentId, request);
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_SAVE_AVAILABLE_TIME_SELECTION));
    }
}
