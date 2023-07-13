package com.seah.specialsteel.controller;



import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.dto.UploadResultDTO;
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
import java.util.List;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    private final FileService fileService;

    List<String> uploadResultList;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<String>> uploadfile(MultipartFile[] uploadfiles) throws Exception {
        uploadResultList = fileService.uploadResult(uploadfiles);
        return new ResponseEntity<>(uploadResultList, HttpStatus.OK);
    }

    @PostMapping("/sendFileName")
    public ResponseEntity<ResultDTO> receiveResultDTO(String fileName) throws IOException, ParseException {
        String realFileName = uploadResultList.get(Integer.parseInt(fileName));
        ResultDTO resultDTO = new ResultDTO(realFileName);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);

    }
}
