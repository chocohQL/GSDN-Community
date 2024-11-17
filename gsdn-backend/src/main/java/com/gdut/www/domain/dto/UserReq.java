package com.gdut.www.domain.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gdut.www.common.Constants.PATTERN_PASSWORD;
import static com.gdut.www.common.ResponseMsg.VALIDATE_PASSWORD;
import static com.gdut.www.common.ResponseMsg.VALIDATE_USERNAME;

/**
 * @author chocoh
 */
@Data
public class UserReq {
    @Size(min = 1, max = 16, message = VALIDATE_USERNAME)
    private String username;
    @Pattern(regexp = PATTERN_PASSWORD, message = VALIDATE_PASSWORD)
    private String password;
    private String avatar;
    private String intro;
}
