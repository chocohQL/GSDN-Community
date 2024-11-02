package com.gdut.www.service.impl;

import com.gdut.www.client.OssClient;
import com.gdut.www.common.ResponseMsg;
import com.gdut.www.exception.GlobalException;
import com.gdut.www.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author chocoh
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private OssClient ossClient;

    @Override
    public String uploadImg(MultipartFile file) {
        try {
            String url = ossClient.uploadImg(file);
            if (url == null) {
                throw new GlobalException(ResponseMsg.FILE_UPLOAD_FORBID);
            } else {
                return url;
            }
        } catch (IOException e) {
            throw new GlobalException(ResponseMsg.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public void deleteImg(String url) {
        ossClient.deleteImg(url);
    }
}
