package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.ResultDTO;

import com.seah.specialsteel.service.HistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.seah.specialsteel.dto.CompareDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    List<ResultDTO> oriResultDTOList;
    List<ResultDTO> revResultDTOList;

    private final HistoryService historyService;

    @GetMapping("/initResultDtoList")
    public ResponseEntity<String> initResultDtoList() {
        oriResultDTOList = new ArrayList<>();
        revResultDTOList = new ArrayList<>();
        return new ResponseEntity<>("초기화 완료", HttpStatus.OK);
    }

    @PostMapping("/oriResponseData")
    public ResponseEntity<List<String>> oriResponseData(@RequestBody()String oriResponseData) throws Exception {
        List<String> test = new ArrayList<>();

        ResultDTO resultDTO = new ResultDTO(oriResponseData);
        oriResultDTOList.add(resultDTO);

        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @PostMapping("/revResponseData")
    public ResponseEntity<List<String>> revResponseData(@RequestBody()String revResponseData) throws Exception {
        List<String> test = new ArrayList<>();

        ResultDTO resultDTO = new ResultDTO(revResponseData);
        revResultDTOList.add(resultDTO);

        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    // 차이가 없을경우 리스트에서 제거하는 부분
    @GetMapping("/filterList")
    public ResponseEntity<String> filterList() {
        if (!oriResultDTOList.isEmpty()) {
            for(int i = 0; i < oriResultDTOList.size(); i++) {
                CompareDTO compareDTO = new CompareDTO(oriResultDTOList.get(i), revResultDTOList.get(i));
                if(compareDTO.diffAlloyInputs.isEmpty() && compareDTO.diffMaterials.isEmpty()){
                    oriResultDTOList.remove(i);
                    revResultDTOList.remove(i);
                    i--;
                }
            }
        }
        return new ResponseEntity<>("필터 완료", HttpStatus.OK);
    }

    @GetMapping("/showList")
    public ResponseEntity<Integer> responseSuccess() {
        return new ResponseEntity<>(oriResultDTOList.size(), HttpStatus.OK);
    }

    //기존 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendOriFileName")
    public ResponseEntity<ResultDTO> receiveOriResultDTO(@RequestParam ("index")int index) {
        return new ResponseEntity<>(oriResultDTOList.get(index), HttpStatus.OK);
    }

    //수정 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendRevFileName")
    public ResponseEntity<Map<String, Object>> receiveRevResultDTO(@RequestParam ("index")int index) {
        revResultDTOList.get(index).setLength(revResultDTOList.size());
        ResultDTO revResultDTO = revResultDTOList.get(index);

        Map<String, Object> response = new HashMap<>();
        response.put("revResultDTO", revResultDTO);

        if (!oriResultDTOList.isEmpty()) {
            CompareDTO compareDTO = new CompareDTO(oriResultDTOList.get(index), revResultDTO);
            response.put("compareDTO", compareDTO);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/allSaveHistory")
    public ResponseEntity<String> allSaveHistory(@RequestParam ("title")String title) {
        if(!oriResultDTOList.isEmpty()) {
            historyService.saveAllHistory(title, oriResultDTOList, revResultDTOList);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}