package com.seah.specialsteel.controller;

import com.seah.specialsteel.tools.CompareResult;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CompareController {
    @PostMapping("/result")
    public ResponseEntity<CompareResult> compare(@RequestParam("originalFile") MultipartFile originalFile,
                                                 @RequestParam("modifiedFiles") MultipartFile[] modifiedFiles) throws IOException, ParseException {
        // 파일 비교 로직을 구현하고 CompareResult 객체를 생성하여 결과를 반환합니다.
        CompareResult compareResult = compareFiles(originalFile, modifiedFiles);

        return new ResponseEntity<>(compareResult, HttpStatus.OK);
    }

    private CompareResult compareFiles(MultipartFile originalFile, MultipartFile[] modifiedFiles) throws IOException, ParseException {
        // 파일 비교 로직을 구현하고 CompareResult 객체를 생성하여 결과를 반환합니다.
        CompareResult compareResult = new CompareResult();
        compareResult.setModifiedFiles(getModifiedFileNames(modifiedFiles));

        return compareResult;
    }

    private List<String> getModifiedFileNames(MultipartFile[] modifiedFiles) {
        // 업로드된 수정된 알고리즘 파일 개수에 따라 파일 이름 목록을 생성합니다.
        List<String> modifiedFileNames = new ArrayList<>();
        for (MultipartFile modifiedFile : modifiedFiles) {
            modifiedFileNames.add(modifiedFile.getOriginalFilename());
        }
        return modifiedFileNames;
    }
}

