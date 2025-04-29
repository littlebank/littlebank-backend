package com.littlebank.finance.domain.relationship.controller;

import com.littlebank.finance.domain.relationship.dto.request.RelationshipRequest;
import com.littlebank.finance.domain.relationship.dto.response.RelationshipResponse;
import com.littlebank.finance.domain.relationship.service.RelationshipService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
