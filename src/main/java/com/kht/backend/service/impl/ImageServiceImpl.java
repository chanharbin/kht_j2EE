package com.kht.backend.service.impl;

import com.kht.backend.entity.Result;
import com.kht.backend.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public Result saveImage(MultipartFile multipartFile) {
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("imageUrl","");
        return Result.OK(resultData).build();
    }
}
