// TotalStats.ts
export interface HomeData {
    userLoginLog: UserLoginLog;
    totalStats: TotalStats;
    sevenTotalStats: TotalStats[]; // 你需要根据实际情况定义这个类型
    newFileStatistics: FileIncludeNameStatistics[];
    deviceStatistics : DeviceStatistics;
    
  }
export interface TotalStats {
    id: number;
    totalUpload: number;
    totalDownload: number;
    totalDelete: number;
    userUuid: string;
    date: Date;
  }
  
  // UserLoginLog.ts
  export interface UserLoginLog {
    id?: number | null; // 可选属性
    lastLoginTime: Date | string;
    lastLoginIp: string;
    lastLoginDevice: string ;
    userUuid: string;
  }
  
  // FileStatistics.ts
  export interface FileStatistics {
    id: number;
    fileId: number;
    referenceCount: number;
    userUuid: string;
    date: Date | string;
  }
  export interface FileIncludeNameStatistics extends FileStatistics{
    imageName : string;
  }
  export interface DeviceStatistics {
    [key: string]: any;
    id: number;
    windows: number;
    mac: number;
    linux: number;
    ios: number;
    android: number;
    other: number;
    userUuid: string;
  }
  
  
  