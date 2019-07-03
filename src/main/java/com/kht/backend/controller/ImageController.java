package com.kht.backend.controller;

import com.kht.backend.entity.Result;
import com.kht.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @ResponseBody
    @RequestMapping(value = "/image", method = POST)
    public Result saveImage(@RequestParam("file") MultipartFile files[], HttpServletRequest httpServletRequest) {


        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("imageUrl","");
        return Result.OK(resultData).build();
    }
}
