package com.encore.ordering.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginReqDto
{
    @NotEmpty(message = "email is required")
    @Email(message = "email is required")
    private String email;
    @NotEmpty(message = "password is required")
    @Size(min = 4, message = "minimun length is 4")
    private String password;
}
