export interface PageFiles{
    folderList : Folder[];
    imageList : image[];
}

export interface image{
    id: number;
    imageName: string;
    folderUuid: string;
    userUuid: string;
    imageSize: string;
    thumbnailData: Uint8Array;
    md5: string;
    uploadTime: string;
    lastModifiedAt: string;
}
export interface Folder{
    id: number;
    uuid: string;
    folderName: string;
    path: string;
    parentFolderUuid: string;
    userUuid: string;
    createdAt: string;
    lastModifiedAt: string;          
}


export interface TableElement{
    name : string;
    img : string;
    size : string;
    md5 : string | null;
    time : string;
    thumbnailData: Uint8Array | null;
    url : string | null;
}
export interface FolderModel{
    value : string,
    label : string,
    children : FolderModel[]
}
export type TableElementOrNull = TableElement | null;
