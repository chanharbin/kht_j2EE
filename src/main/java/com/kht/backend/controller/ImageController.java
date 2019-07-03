package com.kht.backend.controller;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.ImageService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image", method = POST)
    public Result saveImage(@RequestParam("images") MultipartFile images[]) {
        List<String> imageUrlList = new ArrayList<>();
        try {
            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        String name = image.getOriginalFilename();
                        String ext = name.substring(name.lastIndexOf("."));
                        String sha1 = DigestUtils.sha1Hex(image.getInputStream());
                        String fileName = sha1 + ext;
                        String path = System.getProperty("java.io.tmpdir") + File.separator + imageService.sha1ToPath(sha1);
                        imageService.mkdirs(path);
                        FileUtils.copyInputStreamToFile(image.getInputStream(), new File(path, fileName));
                        String imageUrl = imageService.uploadImage("http://119.23.239.101:4869/upload", path + File.separator + fileName);
                        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                            imageUrlList.add(imageUrl);
                            File file = new File(path, fileName);
                            file.setWritable(true);
                            System.gc();
                            file.delete();
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "图片上传失败");
        }
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("imageUrlList", imageUrlList);
        return Result.OK(resultData).build();
    }
}
