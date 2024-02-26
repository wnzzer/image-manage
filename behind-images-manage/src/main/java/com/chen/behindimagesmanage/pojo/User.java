package com.chen.behindimagesmanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15031
 * 用户类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  {
   private int id;
   private String username;
   private String password;
   private String uuid;
   private int level;
   private byte[] thumbnailAvatar;
   public User(User user){
      this.username = user.getUsername();
      this.password = user.getPassword();
      this.uuid = user.getUuid();
      this.level = user.getLevel();
      this.thumbnailAvatar = user.getThumbnailAvatar();

   }

   public User(String username, String password, String uuid, int level) {
      this.username = username;
      this.password = password;
      this.uuid = uuid;
      this.level = level;
   }
}
