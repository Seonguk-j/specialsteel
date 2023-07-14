package com.seah.specialsteel.controller;



import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.dto.UploadResultDTO;
import com.seah.specialsteel.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    private final FileService fileService;

    List<String> uploadResultList;
    static ResultDTO oriResultDTO;
    List<ResultDTO> revResultDTOList = new ArrayList<>();

    //파일 저장
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<String>> uploadfile(MultipartFile[] uploadfiles) throws Exception {
        uploadResultList = fileService.uploadResult(uploadfiles);
        return new ResponseEntity<>(uploadResultList, HttpStatus.OK);
    }
    //기존 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendOriFileName")
    public ResponseEntity<ResultDTO> receiveOriResultDTO(String fileName) throws IOException, ParseException {
        String realFileName = uploadResultList.get(Integer.parseInt(fileName));
        oriResultDTO = new ResultDTO(realFileName);
        return new ResponseEntity<>(oriResultDTO, HttpStatus.OK);
    }
    //수정 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendRevFileName")
    public ResponseEntity<ResultDTO> receiveRevResultDTO(String fileName) throws IOException, ParseException {
        String realFileName = uploadResultList.get(Integer.parseInt(fileName));
        if(revResultDTOList.isEmpty())
            revResultDTOList.add(new ResultDTO(realFileName));
        else
            revResultDTOList.set(0, new ResultDTO(realFileName));
        return new ResponseEntity<>(revResultDTOList.get(0), HttpStatus.OK);
    }
//    @PostMapping("/compareResult")
//    public ResponseEntity<ResultDTO> compareResult() {
//        HashMap<String,String> ori = resultDTOList.get(0).expectMaterials;
//        HashMap<String,String> rev = resultDTOList.get(1).expectMaterials;
//
//    }
}
