package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation extends User{
    private String token;
    public UserInformation(User user,String token){
        super(user);
        this.token = token;
    }
}
