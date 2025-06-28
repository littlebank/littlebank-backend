package com.littlebank.finance.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuggestChildDto {
    private Long childId;
    private String childUserName;

    public SuggestChildDto (Long childId, String childUserName) {
        this.childId = childId;
        this.childUserName = childUserName;
    }
}
