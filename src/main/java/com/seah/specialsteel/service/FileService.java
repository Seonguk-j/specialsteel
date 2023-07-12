package com.seah.specialsteel.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    @Value("${uploadPath}")
    String uploadPath;

    public Path uploadPath(String originalFileName) throws Exception{

        String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\") +1);

        log.info("fileName: " +fileName);

        //날짜 생성 폴더 생성
        String folderPath = makeFolder();

        //UUID
        String uuid = UUID.randomUUID().toString();

        //저장할 파일 이름 중간에 "_"를 이용해서 구분
        String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(saveName);

        return savePath;

}

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        //make folder
        File uploadPathFolder = new File(uploadPath,folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
