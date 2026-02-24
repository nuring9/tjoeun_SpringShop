package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service //Spring이 이 클래스를 서비스 Bean으로 등록하도록 함.
@Log  // Lombok 로그 기능 사용.
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)
        throws Exception{
        UUID uuid = UUID.randomUUID(); // 랜덤으로 UUID 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath+"/"+savedFileName;
        System.out.println(fileUploadFullUrl);
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){  //  deleteFile 객체 여부를 확인
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
