package com.littlebank.finance.domain.notification.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuggestChildDto {
    private Long childId;
    private String childUserName;

    public SuggestChildDto (Long childId, String childUserName) {
        this.childId = childId;
        this.childUserName = childUserName;
    }
}
