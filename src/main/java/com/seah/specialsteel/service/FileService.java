package com.seah.specialsteel.service;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.dto.UploadResultDTO;
import com.seah.specialsteel.tools.ExtractJson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    @Value("${com.seah.upload.path}")
    String uploadPath;

    public List<String> uploadResult(MultipartFile[] uploadfiles) throws Exception{

        List<String> uploadResultList = new ArrayList<>();

        for(MultipartFile uploadfile : uploadfiles) {
            String originalName = uploadfile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);

            //날짜 생성 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
//            String saveName = uploadPath + File.separator + File.separator + folderPath + File.separator + File.separator + uuid + "_" + fileName;
            String saveName = uploadPath  + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                uploadfile.transferTo(savePath);
                uploadResultList.add(saveName);
                log.info(uploadResultList.get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadResultList;

}

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator );

        //make folder
        File uploadPathFolder = new File(uploadPath,folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

}
