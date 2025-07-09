package com.littlebank.finance.domain.user.dto.request;

import com.littlebank.finance.domain.user.domain.SchoolType;
import com.littlebank.finance.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterSchoolRequest {
    private String schoolName;
    private SchoolType schoolType;
    private Integer region;
    private String address;

    public User toEntity () {
        return User.builder()
                .schoolName(schoolName)
                .schoolType(schoolType)
                .region(region)
                .address(address)
                .build();
    }
}
