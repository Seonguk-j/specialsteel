package com.seah.specialsteel.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public void deleteFile(String filePath) throws Exception{
        Path path = Path.of(filePath);

        // 파일 존재 여부 확인
        if (Files.exists(path)) {
            // 파일 채널 열기
            try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE)) {
                // 파일 잠금 시도
                FileLock lock = channel.tryLock();
                if (lock != null) {
                    // 잠금 성공: 파일 삭제
                    Files.delete(path);
                    log.info("파일을 삭제하였습니다.");
                } else {
                    // 파일이 사용 중인 경우
                    log.info("파일이 사용 중입니다.");
                }
            }
        } else {
            // 파일이 존재하지 않는 경우
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
