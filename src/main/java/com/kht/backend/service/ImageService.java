package com.kht.backend.service;

public interface ImageService {

    public String uploadImage(String url, String file);

    public void mkdirs(String path);

    public String sha1ToPath(String sha1);

    public String readExt(String image);

}
