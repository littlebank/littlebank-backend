package com.littlebank.finance.domain.school.controller.admin;

import com.littlebank.finance.domain.school.dto.response.SchoolSyncResponse;
import com.littlebank.finance.domain.school.service.admin.AdminSchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-admin/school")
public class AdminSchoolController {
    private final AdminSchoolService schoolService;

    @PostMapping("/store")
    public ResponseEntity<SchoolSyncResponse> syncAllSchoolData() {
        SchoolSyncResponse response = schoolService.fetchAndSaveAllSchools(1000);
        return ResponseEntity.ok(response);
    }
}