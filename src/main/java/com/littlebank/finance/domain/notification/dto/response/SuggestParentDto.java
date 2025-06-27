package com.littlebank.finance.domain.notification.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuggestParentDto {
    private Long parentId;
    private String childNickName;
    private String childId;

    public SuggestParentDto(Long parentId, String childNickName, String childId) {
        this.parentId = parentId;
        this.childNickName = childNickName;
        this.childId = childId;
    }
}
