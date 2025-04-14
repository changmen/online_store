package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.enums.GenderType;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MemberRegistryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7490516187847553182L;

    @NotNull(message = "姓名字段不能为空")
    @Pattern(regexp = Constants.MEMBER_NAME_PATTERN, message = "姓名格式不正确,应为2-16位的中文、英文或数字")
    private String name;

    @NotNull(message = "密码字段不能为空")
    @Pattern(regexp = Constants.MEMBER_PASSWORD_PATTERN, message = "密码格式不正确,应为8-16位字母、数字和特殊字符的组合")
    private String password;

    @Size(max = 16, message = "昵称长度不能大于16")
    private String nickName;

    @NotNull(message = "性别字段不能为空")
    private GenderType gender;

    @Min(value = 18, message = "年龄必须大于等于18")
    private int age;

    @NotNull(message = "手机号码字段不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "手机号码格式不正确,应为11位有效手机号")
    private String phone;

}
