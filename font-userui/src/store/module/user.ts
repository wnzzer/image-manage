import { defineStore } from "pinia";
// 定义 tabList 中的每个 tab 对象的类型

interface User {
    username: string;
    password: string;
    uuid: string;
    level: number;
    token: string;
    thumbnailAvatar : Uint8Array | null;


  }
  
  interface UserState {
    user: User;
  }
  
  const useUserStore = defineStore('user', {
      state: (): UserState => {
        return {
          user: {
            token : '',
            username: '',
            password: '',
            uuid: '',
            level: 0,
            thumbnailAvatar : null

          },
        }
      },
      actions : {
        clearToken(){
          this.user = {
            token : '',
            username: '',
            password: '',
            uuid: '',
            level: 0,
            thumbnailAvatar : null
          }
         }
      },
      persist: true
  })
  export default useUserStore;