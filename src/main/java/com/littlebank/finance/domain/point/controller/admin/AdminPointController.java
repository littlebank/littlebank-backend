package com.littlebank.finance.domain.point.controller.admin;

import com.littlebank.finance.domain.point.dto.response.admin.RefundHistoryResponse;
import com.littlebank.finance.domain.point.service.AdminPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-admin/point")
@RequiredArgsConstructor
@Tag(name = "Point")
public class AdminPointController {
    private final AdminPointService adminPointService;

    @Operation(summary = "모든 유저 포인트 환전 내역 조회 API")
    @GetMapping("/refund/history")
    public ResponseEntity<List<RefundHistoryResponse>> getUsersRefundHistory() {
        List<RefundHistoryResponse> response = adminPointService.getUsersRefundHistory();
        return ResponseEntity.ok(response);
    }
}
