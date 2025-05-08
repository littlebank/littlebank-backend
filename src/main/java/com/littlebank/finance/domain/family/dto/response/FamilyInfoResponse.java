package com.littlebank.finance.domain.family.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FamilyInfoResponse {
    private Long familyId;
    private List<FamilyMemberInfoResponse> memberInfoList;

}
