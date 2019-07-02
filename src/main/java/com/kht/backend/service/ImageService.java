package com.kht.backend.service;

import com.kht.backend.entity.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public Result saveImage(MultipartFile multipartFile);

}
