package com.seah.specialsteel.controller;

import com.seah.specialsteel.service.ChangeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class ChangeFileSaveController {

    @Autowired
    private ChangeDataService changeDataService;

    @PostMapping("/sendExcelCsv")
    public ResponseEntity<Object> processFile(@RequestParam("file") MultipartFile file, @RequestParam("mode") String mode) throws IOException {

        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", mode);

        String filename = file.getOriginalFilename();
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        ArrayList<ArrayList<Object>> outputData = null;
        ArrayList<ArrayList<Object>> csvOutPutData = null;
        if (fileExtension.equals("xlsx") || fileExtension.equals("xls")) {
            ArrayList<ArrayList<Object>> excelData = changeDataService.parseExcelFileToArrayList(file);
            if(mode.equals("standardizationRadio")){
                outputData = changeDataService.standardizeExcelData(excelData);
            }else{
                outputData = changeDataService.normalizeExcelData(excelData);
            }
            map.put("data", outputData);
            // 정규화,표준화된 데이터를 JSON 형식으로 반환
            return new ResponseEntity<>(map, HttpStatus.OK);

        } else if (fileExtension.equals("csv")) {
            ArrayList<ArrayList<Object>> csvData = changeDataService.parseCsvFileToArrayList(file);
            if(mode.equals("standardizationRadio")){
                csvOutPutData = changeDataService.standardizeExcelData(csvData);
            }else{
                csvOutPutData = changeDataService.normalizeExcelData(csvData);
            }
            map.put("data", csvOutPutData);
            // 정규화,표준화된 데이터를 JSON 형식으로 반환
            return new ResponseEntity<>(map, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/saveData")
    public void saveData(@RequestBody Map<String, Object> dataListMap) {
        Map<String, Object> saveDataKeyMap = new HashMap<>();

        List<List<List<Object>>> dataList = (List<List<List<Object>>>) dataListMap.get("keyData");
        String mode = (String) dataListMap.get("mode");
        saveDataKeyMap.put("mode", mode);
        saveDataKeyMap.put("dataKeyList", dataList);

        changeDataService.saveData(saveDataKeyMap);

    }





}

