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
    static ResultDTO oriResultDTO;
    List<ResultDTO> revResultDTOList;

    private final HistoryService historyService;

    //파일 저장
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<ResultDTO>> resultDTO(MultipartFile[] uploadfiles) throws Exception {
        List<String> uploadResultList = fileService.uploadResult(uploadfiles);
        int i = 0;
        revResultDTOList = new ArrayList<>();

        for (String str : uploadResultList) {
            revResultDTOList.add(new ResultDTO(str));
            revResultDTOList.get(i).setIndex(i);
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
        return new ResponseEntity<>(oriFileName, HttpStatus.OK);
    }

    //기존 알고리즘 json파일을 dto에 담아서 보내기
    @PostMapping("/sendOriFileName")
    public ResponseEntity<ResultDTO> receiveOriResultDTO() {
        return new ResponseEntity<>(oriResultDTO, HttpStatus.OK);
    }

    //수정 알고리즘 json파일을 dto에 담아서 보내기


    @PostMapping("/saveComment")
    public ResponseEntity<Map<String, Object>> saveComment(@RequestParam("index") int index, @RequestParam("saveIndex") int saveIndex, @RequestParam("comment") String comment) {
        revResultDTOList.get(index).setLength(revResultDTOList.size());
        revResultDTOList.get(saveIndex).setComment(comment);

        ResultDTO revResultDTO = revResultDTOList.get(index);

        Map<String, Object> response = new HashMap<>();
        response.put("revResultDTO", revResultDTO);

        if (oriResultDTO != null) {
            CompareDTO compareDTO = new CompareDTO(oriResultDTO, revResultDTO);
            response.put("compareDTO", compareDTO);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/sendRevFileName")
    public ResponseEntity<Map<String, Object>> receiveRevResultDTO(@RequestParam ("index")int index) {
        revResultDTOList.get(index).setLength(revResultDTOList.size());
        ResultDTO revResultDTO = revResultDTOList.get(index);

        Map<String, Object> response = new HashMap<>();
        response.put("revResultDTO", revResultDTO);

        if (oriResultDTO != null) {
            CompareDTO compareDTO = new CompareDTO(oriResultDTO, revResultDTO);
            response.put("compareDTO", compareDTO);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteList")
    public ResponseEntity<ResultDTO> deleteFile(String index){

        revResultDTOList.remove(Integer.parseInt(index));
        if(revResultDTOList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            revResultDTOList.get(0).setLength(revResultDTOList.size());
            for (int i = 0; i < revResultDTOList.size(); i++) {
                revResultDTOList.get(i).setIndex(i);
            }
            return new ResponseEntity<>(revResultDTOList.get(0), HttpStatus.OK);
        }
    }

    @PostMapping("/saveHistory")
    public ResponseEntity<String>saveHistory(@RequestParam ("index")int index, @RequestParam ("revComment")String revComment, @RequestParam ("oriComment")String oriComment) {
        if(oriResultDTO != null) {
            revResultDTOList.get(index).setComment(revComment);
            oriResultDTO.setComment(oriComment);

            historyService.saveHistory(oriResultDTO, revResultDTOList.get(index));
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/allSaveHistory")
    public ResponseEntity<String>allSaveHistory(@RequestParam ("index") int index, @RequestParam ("revComment")String revComment, @RequestParam ("oriComment")String oriComment) {
        if(oriResultDTO != null) {
            revResultDTOList.get(index).setComment(revComment);
            oriResultDTO.setComment(oriComment);
            historyService.saveAllHistory(oriResultDTO, revResultDTOList);

            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
