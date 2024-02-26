import { defineStore } from "pinia";
// 定义 tabList 中的每个 tab 对象的类型
export interface TabItem {
    title: string;
    path: string;
    icon: any; // 这里可以根据实际情况指定 icon 的类型
}

// 定义 state 的类型
interface HeaderState {
    tab:   {
        isCollapse: boolean;
        tabList: TabItem[];
    };
}

// 使用 defineStore 定义 Store
const useHeaderStore = defineStore({
    id: 'header',
    state: (): HeaderState => {
        return {
            tab: {
                isCollapse: false,
                tabList: [
                    {
                        title: '首页',
                        path: '/userCenter/home',
                        icon: null // 填入正确的 icon 类型
                    }
                ]
            }
        };
    },
    actions : {
        closeTag(item : TabItem){
            this.tab.tabList = this.tab.tabList.filter((ele)=>{
                return item.title != ele.title;
            })  
        }
        
        
        
    }
});
export default useHeaderStore;
