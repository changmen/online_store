package com.example.onlinestore.bean;

import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.enums.GenderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class MemberBaseInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 7202771544778274481L;
    /**
     * 用户真实姓名
     */
    @NotNull(message = "姓名字段不能为空")
    @Pattern(regexp = Constants.MEMBER_NAME_PATTERN, message = "姓名格式不正确,应为2-16位的中文、英文或数字")
    private String name;
    /**
     * 用户昵称（非必填）
     */
    @Size(max = 16, message = "昵称长度不能大于16")
    private String nickName;
    /**
     * 用户登录密码（明文或加密存储形式）
     */
    @NotNull(message = "密码字段不能为空")
    @Pattern(regexp = Constants.MEMBER_PASSWORD_PATTERN, message = "密码格式不正确,应为8-16位字母、数字和特殊字符的组合")
    private String password;
    /**
     * 用户绑定的手机号码
     */
    @NotNull(message = "手机号码字段不能为空")
    @Pattern(regexp = Constants.PHONE_PATTERN, message = "手机号码格式不正确,应为11位有效手机号")
    private String phone;
    /**
     * 用户性别（枚举值，如 MALE/FEMALE/UNKNOWN）
     */
    @NotNull(message = "性别字段不能为空")
    private GenderType gender;
    /**
     * 用户年龄（有效值应大于等于18）
     */
    @Min(value = 18, message = "年龄必须大于等于18")
    private int age;

}
