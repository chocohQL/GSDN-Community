package com.gdut.www.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author chocoh
 */
public interface FileService {

    /**
     * 上传图片
     * @param file 图片文件
     * @return url
     */
    String uploadImg(MultipartFile file);

    /**
     * 删除图片
     * @param url 图片路径
     */
    void deleteImg(String url);
}
