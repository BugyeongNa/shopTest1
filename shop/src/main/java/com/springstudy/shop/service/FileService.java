package com.springstudy.shop.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    public String uploadFile(
            String uploadPath,
            String originalFileName,
            byte[] fileDate) throws Exception{

        UUID uuid = UUID.randomUUID();
        // "/images/test/abc/image/abc.png"
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath+"/"+savedFileName; // 업로드할경로+"/"+UUID값+".확장자"

        // 업로드 완료
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileDate);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws  Exception{
        File deleteFile = new File(filePath);

        if (deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하셨습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
