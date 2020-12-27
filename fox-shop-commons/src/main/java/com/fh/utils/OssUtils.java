package com.fh.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName OssUtils
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/3 11:22
 * @Version 1.0
 **/
public class OssUtils {

    @SneakyThrows
    public static String uploadPhoto(String mkdir, MultipartFile file) throws IOException {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GCbNt4d3fT3uE4xhJZZ";
        String accessKeySecret = "iwFy2PkLU69I8PgjfpulYJaXGkzSc1";
        String photoUrl="https://fhwork2005.oss-cn-beijing.aliyuncs.com/";
        String bucketName = "fhwork2005";
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String oldFileName = file.getOriginalFilename();
        String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString()+suffix;

        String path =mkdir+"/"+newFileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName,path,file.getInputStream());
        // 关闭OSSClient。
        ossClient.shutdown();

        return photoUrl+"/"+path;
    }
}
