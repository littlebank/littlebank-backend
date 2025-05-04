package com.littlebank.finance.domain.relationship.controller;

import com.littlebank.finance.domain.relationship.dto.request.RelationshipReqAcceptRequest;
import com.littlebank.finance.domain.relationship.dto.request.RelationshipRequest;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipReqAcceptResponse;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipRequestsReceivedResponse;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipResponse;
import com.littlebank.finance.domain.relationship.service.RelationshipService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/relationship")
@RequiredArgsConstructor
@Tag(name = "Relationship")
public class RelationshipController {
    private final RelationshipService relationshipService;

    @Operation(summary = "관계 요청 API")
    @PostMapping
    public ResponseEntity<RelationshipResponse> createRelationship(
            @RequestBody @Valid RelationshipRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        RelationshipResponse response = relationshipService.createRelationship(request, customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "요청 온 관계 내역 조회 API")
    @GetMapping("/inbox")
    public ResponseEntity<List<RelationshipRequestsReceivedResponse>> getRelationshipRequestsReceived(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<RelationshipRequestsReceivedResponse> response = relationshipService.getReceivedRelationshipRequests(customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "관계 요청 수락 API")
    @PatchMapping("/accept")
    public ResponseEntity<RelationshipReqAcceptResponse> acceptRelationshipRequest(
            @RequestBody @Valid RelationshipReqAcceptRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        RelationshipReqAcceptResponse response = relationshipService.acceptRelationshipRequest(request, customUserDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "관계 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(
            @Parameter(description = "조회할 유저 ID")
            @PathVariable("id") Long relationshipId
    ) {
        relationshipService.deleteRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }
}
