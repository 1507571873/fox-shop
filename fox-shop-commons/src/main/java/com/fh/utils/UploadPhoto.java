package com.fh.utils;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName UploadPhoto
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/3 11:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("/UploadPhoto")
public class UploadPhoto {

    @PostMapping
    public CommonsReturn uploadPhoto(@PathVariable("file") MultipartFile file) throws IOException {
        String url = OssUtils.uploadPhoto("user", file);
        return CommonsReturn.success(url);
    }

}
