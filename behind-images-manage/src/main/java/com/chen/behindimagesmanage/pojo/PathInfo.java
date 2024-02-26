package com.chen.behindimagesmanage.pojo;

import com.chen.behindimagesmanage.util.VerificationUtil;

/**
 * @author 15031
 */
public class PathInfo {
    private String path;
    private boolean isFile;
    private boolean isDirectory;
    private String fileName;
    private String directoryName;
    private String prefixDirectory  = "/";

    public PathInfo(String path) {
        this.path = path;
        if(VerificationUtil.isValidFilePath(path)){
           String newPath = path.substring(1);
            String[]pathParts = newPath.split("/");
            String name = pathParts[pathParts.length - 1];
            if (name.contains(".")){
                fileName = name;
                isFile = true;
            }else {
                directoryName = name;
                isDirectory = true;
            }
            if(pathParts.length >= 2){
                StringBuilder parentD = new StringBuilder();
                for(int i = 0; i < pathParts.length - 1; i++){
                    parentD.append("/").append(pathParts[i]);
                }
                prefixDirectory = parentD.toString();
            }

        }
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getPrefixDirectory() {
        return prefixDirectory;
    }
}