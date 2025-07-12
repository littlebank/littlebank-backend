package com.littlebank.finance.domain.point.controller.admin;

import com.littlebank.finance.domain.point.dto.response.admin.ChargeHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.admin.RefundHistoryResponse;
import com.littlebank.finance.domain.point.service.admin.AdminPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-admin/point")
@RequiredArgsConstructor
@Tag(name = "Point")
public class AdminPointController {
    private final AdminPointService adminPointService;

    @Operation(summary = "모든 유저 포인트 충전 결제 내역 조회 API")
    @GetMapping("/charge/history")
    public ResponseEntity<List<ChargeHistoryResponse>> getUsersChargeHistory() {
        List<ChargeHistoryResponse> response = adminPointService.getUsersChargeHistory();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 유저 포인트 환전 내역 조회 API")
    @GetMapping("/refund/history")
    public ResponseEntity<List<RefundHistoryResponse>> getUsersRefundHistory() {
        List<RefundHistoryResponse> response = adminPointService.getUsersRefundHistory();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "포인트 환전 처리 완료 상태로 업데이트 API")
    @PatchMapping("/refund/sent-money/{refundId}")
    public ResponseEntity<String> updateProcessedStatus(
            @Parameter(description = "환전 내역 데이터 식별 id")
            @PathVariable(name = "refundId") Long refundId
    ) {
        adminPointService.updateProcessedStatus(refundId);
        return ResponseEntity.ok("환전 처리 완료");
    }
}
