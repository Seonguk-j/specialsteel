package com.seah.specialsteel.controller;



import com.seah.specialsteel.dto.UploadResultDTO;
import com.seah.specialsteel.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    private final FileService fileService;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadfile(MultipartFile[] uploadfiles) throws Exception {
        List<UploadResultDTO> uploadResultDTOList = fileService.uploadResultDTOList(uploadfiles);
        return new ResponseEntity<>(uploadResultDTOList, HttpStatus.OK);
    }
}
