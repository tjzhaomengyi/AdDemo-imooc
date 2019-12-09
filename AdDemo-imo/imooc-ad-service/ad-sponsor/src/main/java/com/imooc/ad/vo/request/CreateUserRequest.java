package com.imooc.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:36
 * @Description:创建用户请求
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String username;

    public boolean validate(){
        return !StringUtils.isEmpty(username);
    }
}
