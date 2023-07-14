package com.seah.specialsteel.controller;



import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    private final FileService fileService;

//    List<String> uploadResultList;
    static ResultDTO oriResultDTO;
    List<ResultDTO> revResultDTOList;

    //파일 저장
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<ResultDTO>> resultDTO(MultipartFile[] uploadfiles) throws Exception {
        List<String> uploadResultList = fileService.uploadResult(uploadfiles);
        int i = 0;
        revResultDTOList = new ArrayList<>();

        for(String str : uploadResultList){
            revResultDTOList.add(new ResultDTO(str));
            revResultDTOList.get(i).setIndex(i+"");
            i++;
            fileService.deleteFile(str);
        }
        return new ResponseEntity<>(revResultDTOList, HttpStatus.OK);
    }
    @PostMapping("/oriUploadAjax")
    public ResponseEntity<List<String>> oriUploadfile(MultipartFile[] uploadfiles) throws Exception {
        List<String> oriFileName = fileService.uploadResult(uploadfiles);

        oriResultDTO = new ResultDTO(oriFileName.get(0));

        fileService.deleteFile(oriFileName.get(0));
        return new ResponseEntity<>(oriFileName,HttpStatus.OK);
    }

    //기존 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendOriFileName")
    public ResponseEntity<ResultDTO> receiveOriResultDTO() {
        return new ResponseEntity<>(oriResultDTO, HttpStatus.OK);
    }

    //수정 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendRevFileName")
    public ResponseEntity<ResultDTO> receiveRevResultDTO(String index){
        revResultDTOList.get(Integer.parseInt(index));

        return new ResponseEntity<>(revResultDTOList.get(Integer.parseInt(index)), HttpStatus.OK);
    }

    @PostMapping("/deleteList")
    public ResponseEntity<List<ResultDTO>> deleteFile(String index){
        System.out.println("확인용" + index);
        revResultDTOList.remove(Integer.parseInt(index));


        for(int i=0; i<revResultDTOList.size(); i++){
            revResultDTOList.get(i).setIndex(i+"");
        }
        return new ResponseEntity<>(revResultDTOList, HttpStatus.OK);
    }
}
