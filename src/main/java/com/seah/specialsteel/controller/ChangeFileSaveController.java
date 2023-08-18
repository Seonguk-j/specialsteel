package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.ChangeDataDTO;
import com.seah.specialsteel.dto.DataDTO;
import com.seah.specialsteel.service.ChangeDataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ChangeFileSaveController {

    @Autowired
    private ChangeDataService changeDataService;



    @PostMapping("/sendExcelCsv")
    public ResponseEntity<Object> processFile(@RequestParam("file") MultipartFile file, @RequestParam("mode") String mode) throws IOException {
        System.out.println("sendExcelCsv - " + file + " mode - " + mode);

        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", mode);


        String filename = file.getOriginalFilename();
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        ArrayList<ArrayList<ArrayList<Object>>> outputData = null;
        ArrayList<ArrayList<Object>> csvOutPutData = null;
        if (fileExtension.equals("xlsx") || fileExtension.equals("xls")) {
            ArrayList<ArrayList<ArrayList<Object>>> excelData = changeDataService.parseExcel(file);
            System.out.println("엑셀 데이터: " + excelData);

            if(mode.equals("standardizationRadio")){
                outputData = changeDataService.standardizeExcelData(excelData);
                System.out.println("표준화된 엑셀 데이터: " + outputData);
            }else{
                outputData = changeDataService.normalizeExcelData(excelData);
                System.out.println("정규화된 엑셀 데이터: " + outputData);
            }
            map.put("data", outputData);
            // 정규화,표준화된 데이터를 JSON 형식으로 반환
            return new ResponseEntity<>(map, HttpStatus.OK);

        } else if (fileExtension.equals("csv")) {
            ArrayList<ArrayList<Object>> csvData = changeDataService.parseCsvFileToArrayList(file);
            System.out.println("CSV 데이터 : "+csvData);
            if(mode.equals("standardizationRadio")){
                csvOutPutData = changeDataService.standardizeCsvData(csvData);
                System.out.println("표준화된 csv 데이터: " + outputData);
            }else{
                csvOutPutData = changeDataService.normalizeCsvData(csvData);
                System.out.println("정규화된 csv 데이터: " + outputData);
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


        System.out.println("excel변환값저장"+dataListMap);
        changeDataService.saveData(saveDataKeyMap);

    }

    @PostMapping("/saveDecodingKey")
    public void saveDecodingKey(@RequestBody Map<String, Object> dataMap) {
        String mode = (String) dataMap.get("mode");
        List<Object> keyList = (List<Object>) dataMap.get("keyData");
        System.out.println("디코딩 - " + keyList);
        System.out.println("디코딩키 저장 들어옴");
        System.out.println(mode);
        if(mode.equals("standardizationRadio")){
            System.out.println("표준화 디코딩 저장 들어옴");
            changeDataService.saveStandardDecodingkey(keyList);
        }else{
            System.out.println("정규화 디코딩 저장 들어옴");
            changeDataService.saveNormalDecodingkey(keyList);
        }



    }



}

