package com.kht.backend.controller;

import com.kht.backend.entity.Result;
import com.kht.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;


    public Result saveImage(MultipartFile idFront) {
        return imageService.saveImage(idFront);
    }
}
