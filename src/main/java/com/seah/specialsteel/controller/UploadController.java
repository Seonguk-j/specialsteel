package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.service.FileService;

import com.seah.specialsteel.service.HistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.seah.specialsteel.dto.CompareDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Log4j2
@AllArgsConstructor
public class UploadController {
    private final FileService fileService;

    //    List<String> uploadResultList;
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

        log.info("몇번되냐?");
        ResultDTO resultDTO = new ResultDTO(revResponseData);
        revResultDTOList.add(resultDTO);

        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    @GetMapping("/filterList")
    public ResponseEntity<String> filterList() {
        log.info("몇이길래?" + oriResultDTOList.size());
        log.info("얘는 몇이길래?" + revResultDTOList.size());
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


//    @PostMapping("/oriUploadAjax")
//    public ResponseEntity<List<String>> oriUploadfile(MultipartFile[] uploadfiles) throws Exception {
//        List<String> oriFileName = fileService.uploadResult(uploadfiles);
//
//        oriResultDTO = new ResultDTO(oriFileName.get(0));
//
//        fileService.deleteFile(oriFileName.get(0));
//        return new ResponseEntity<>(oriFileName, HttpStatus.OK);
//    }

    //파일 저장
//    @PostMapping("/revUploadAjax")
//    public ResponseEntity<List<ResultDTO>> resultDTO(MultipartFile[] uploadfiles) throws Exception {
//        List<String> uploadResultList = fileService.uploadResult(uploadfiles);
//        int i = 0;
//        revResultDTOList = new ArrayList<>();
//
//        for (String str : uploadResultList) {
//            revResultDTOList.add(new ResultDTO(str));
//            revResultDTOList.get(i).setIndex(i);
//            i++;
//            fileService.deleteFile(str);
//        }
//
//        return new ResponseEntity<>(revResultDTOList, HttpStatus.OK);
//    }


    //기존 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendOriFileName")
    public ResponseEntity<ResultDTO> receiveOriResultDTO(@RequestParam ("index")int index) {
//        log.info("오리지널" + index);
        return new ResponseEntity<>(oriResultDTOList.get(index), HttpStatus.OK);
    }

    //수정 알고리즘 json파일을 dto에 담아서 보내기


//    @PostMapping("/saveComment")
//    public ResponseEntity<Map<String, Object>> saveComment(@RequestParam("index") int index, @RequestParam("saveIndex") int saveIndex, @RequestParam("comment") String comment) {
//        revResultDTOList.get(index).setLength(revResultDTOList.size());
//        revResultDTOList.get(saveIndex).setComment(comment);
//
//        ResultDTO revResultDTO = revResultDTOList.get(index);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("revResultDTO", revResultDTO);
//
//        if (oriResultDTO != null) {
//            CompareDTO compareDTO = new CompareDTO(oriResultDTO, revResultDTO);
//            response.put("compareDTO", compareDTO);
//        }
//
//        return ResponseEntity.ok(response);
//    }


    @PostMapping("/sendRevFileName")
    public ResponseEntity<Map<String, Object>> receiveRevResultDTO(@RequestParam ("index")int index) {
//        System.out.println("왜안되지?" + index);
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
//
//    @PostMapping("/deleteList")
//    public ResponseEntity<ResultDTO> deleteFile(String index){
//
//        revResultDTOList.remove(Integer.parseInt(index));
//        if(revResultDTOList.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }else {
//            revResultDTOList.get(0).setLength(revResultDTOList.size());
//            for (int i = 0; i < revResultDTOList.size(); i++) {
//                revResultDTOList.get(i).setIndex(i);
//            }
//            return new ResponseEntity<>(revResultDTOList.get(0), HttpStatus.OK);
//        }
//    }


    // 원본
//    @PostMapping("/saveHistory")
//    public ResponseEntity<String>saveHistory(@RequestParam ("index")int index,@RequestParam ("revComment")String revComment, @RequestParam ("oriComment")String oriComment,@RequestParam ("title")String title) {
//        if(!oriResultDTO == null) {
//            revResultDTOList.get(index).setComment(revComment);
//            oriResultDTO.setTitle(title);
//            oriResultDTO.setComment(oriComment);
//
//            historyService.saveHistory(oriResultDTO, revResultDTOList.get(index)); --> 원본
//            return new ResponseEntity(HttpStatus.OK);
//        }else {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }


    // 원본
//    @PostMapping("/allSaveHistory")
//    public ResponseEntity<String>allSaveHistory(@RequestParam ("index") int index, @RequestParam ("revComment")String revComment, @RequestParam ("oriComment")String oriComment, @RequestParam ("title")String title) {
//        if(oriResultDTO != null) {
//            revResultDTOList.get(index).setComment(revComment);
//            oriResultDTO.setComment(oriComment);
//            oriResultDTO.setTitle(title);
//            historyService.saveAllHistory(oriResultDTO, revResultDTOList);
//
//            return new ResponseEntity(HttpStatus.OK);
//        }else {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }

    // 수정
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
