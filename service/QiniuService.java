package com.nowcoder.toutiao.service;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/7.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone1());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "tHf3wXN2U3vVB1s9amxe19sf1uh8wJ_6Pe5lWxeP";
    String SECRET_KEY = "v7lmMZhZT-IXm_vxvJcqg9huUmTzStS8anamo9zB";
    //要上传的空间
    String bucketname = "xiaoyizhang";

    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static String QINIU_IMAGE_DOMAIN = "http://oyz4h1nld.bkt.clouddn.com/";
    private static String DOUTIAO_DOMAIN = "http://127.0.0.1:8080";
    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            //System.out.println(res.bodyString());
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}
