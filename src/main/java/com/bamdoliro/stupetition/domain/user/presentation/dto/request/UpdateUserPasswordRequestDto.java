package com.bamdoliro.stupetition.domain.user.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UpdateUserPasswordRequestDto {

    @NotNull(message = "바꿀 비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 20)
    private String password;

    @NotNull(message = "현재 비밀번호를 입력해 주세요.")
    private String currentPassword;
}
