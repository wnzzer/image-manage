export interface PodRunTimeItem {
    type: string;
    status: string;
    message: string;
    reason: string;
    lastUpdateTime: string;
  }
  
  
  export interface PodSimplyInformation {
    workName: string;
    workType: string;
    annotations: string[];
    labels: string[];
    selector: string[];
  }
  
  export interface PodMetaData {
    podSimplyInformation: PodSimplyInformation;
    podRunTimeTable: PodRunTimeItem[];
    yaml : string;
  }
  