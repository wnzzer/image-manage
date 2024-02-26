// simply-user.ts

import { User } from "@element-plus/icons-vue";

export interface SimplyUser {
  id: number;
  uuid: string;
  username: string;
  level: number;
  thumbnailAvatar?: Uint8Array;
  lastLoginTime: Date;
}
export interface User {
  id?: number,
  username?: string,
  password?: string,
  level?: number
  thumbnailAvatar?: Uint8Array;
}


