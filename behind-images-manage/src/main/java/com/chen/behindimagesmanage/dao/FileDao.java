package com.chen.behindimagesmanage.dao;

import com.chen.behindimagesmanage.pojo.ImageMetaData;
import com.chen.behindimagesmanage.pojo.ImageMetaDataAddFileName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 15031
 */
@Mapper
public interface FileDao {
    /**
     * 获取所有图片元数据
     * @return 所有图片元数据
     */
    List<ImageMetaDataAddFileName> getAllImg();

    /**
     * 获取图片元数据
     * @param md5
     * @return 图片元数据
     */
    ImageMetaData getImg(String md5);
    /**
     * 更新元数据归属服务器
     * @param imageMetaData 更新的元数据列
     * @return 影响行
     */
    int updateLocalUrl(ImageMetaData imageMetaData);

    /**
     * 插入md5数据
     * @param md5 图片元数据
     * @param newLocalUrl 新数据
     * @return 影响行数
     */
    int updateAddLocalUrl(@Param("md5")String md5,@Param("newLocalUrl") String newLocalUrl);

    /**
     * 插入md5数据
     * @param imageMetadata 图片元数据
     * @return 影响行数
     */

    int insertImageMetadata(ImageMetaData imageMetadata);


    /**
     * 检查md5是否存在
     * @param md5 md5值
     * @return 影响行数
     */
    int checkIfMd5Exists(String md5);
}
