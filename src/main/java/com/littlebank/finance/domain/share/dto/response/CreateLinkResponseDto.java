package com.littlebank.finance.domain.share.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateLinkResponseDto {
    private List<String> inviteLinks;
}
