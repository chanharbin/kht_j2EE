package com.kht.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.ImageService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String uploadImage(String serverUrl, String file) {
        try {
            String ext = readExt(file);
            URL url = new URL(serverUrl + "/upload");
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(50000);
            connection.setConnectTimeout(25000);
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Content-Type", ext.toLowerCase());
            httpURLConnection.setRequestProperty("COOKIE", "kht");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            OutputStream output = httpURLConnection.getOutputStream();
            FileInputStream input = new FileInputStream(file);
            byte[] byteBuffer = new byte[8192];
            while (true) {
                int length = input.read(byteBuffer);
                if (length <= 0)
                    break;
                output.write(byteBuffer, 0, length);
            }
            StringBuffer stringBuffer = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
            char[] charBuffer = new char[8192];
            while (true) {
                int length = inputStreamReader.read(charBuffer);
                if (length == -1)
                    break;
                char[] buffer = new char[length];
                for (int i = 0; i < length; i++)
                    buffer[i] = charBuffer[i];
                stringBuffer.append(new String(buffer));
            }
            output.close();
            input.close();
            inputStreamReader.close();
            httpURLConnection.disconnect();
            JSONObject json = JSONObject.parseObject(stringBuffer.toString());
            if (json.getBooleanValue("ret")) {
                JSONObject info = json.getJSONObject("info");
                String md5 = info.getString("md5");
                return String.format("%s/%s", serverUrl, md5);
            }
        } catch (Exception exception) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "上传图片失败");
        }
        return null;
    }

    @Override
    public void mkdirs(String path) {
        if (path != null) {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    @Override
    public String sha1ToPath(String sha1) {
        String result = String.valueOf("");
        if (sha1 != null && !sha1.trim().isEmpty()) {
            String s1 = sha1.substring(0, 1);
            String s2 = DigestUtils.sha1Hex(sha1.getBytes()).substring(0, 2);
            result = s1 + File.separator + s2 + File.separator + sha1;
        }
        return result;
    }

    @Override
    public String readExt(String image) {
        try {
            if (image != null && !image.trim().isEmpty()) {
                File file = new File(image);
                if (file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    FileType fileType = FileTypeDetector.detectFileType(bufferedInputStream);
                    return fileType.toString();
                }
            }
        } catch (Exception exception) {
            throw new ServiceException(ErrorCode.FILE_EXT_ERROR, "读取图片ext信息失败");
        }
        return null;
    }

}
