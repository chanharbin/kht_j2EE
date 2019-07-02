package com.kht.backend.controller;

import com.kht.backend.entity.Result;
import com.kht.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;


    @RequestMapping(value = "/image", method = POST, produces = "application/json;charset=UTF-8")
    public Result saveImage(@RequestParam("idFront") MultipartFile idFront) {
        return imageService.saveImage(idFront);
    }
}
