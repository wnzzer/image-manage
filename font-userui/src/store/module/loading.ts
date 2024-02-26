import { defineStore } from "pinia";

interface LoadingState {
    loading : boolean
}
const useLoadingStore = defineStore('loading', {
    state: (): LoadingState => {
      return {
        
          loading : false
        
      }
    },
    actions : {
      
       }
    
})
export default useLoadingStore;