package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.DataDTO;
import com.seah.specialsteel.dto.DateRequestDTO;
import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.ExpectedResultRepository;
import com.seah.specialsteel.repository.OriResultRepository;
import com.seah.specialsteel.repository.RevResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DataController {

    private final OriResultRepository oriResultRepository;
    private final RevResultRepository revResultRepository;
    private final AlloyInputRepository alloyInputRepository;
    private final ExpectedResultRepository expectedResultRepository;

    public DataController(OriResultRepository oriResultRepository,
                          RevResultRepository revResultRepository,
                          AlloyInputRepository alloyInputRepository,
                          ExpectedResultRepository expectedResultRepository) {
        this.oriResultRepository = oriResultRepository;
        this.revResultRepository = revResultRepository;
        this.alloyInputRepository = alloyInputRepository;
        this.expectedResultRepository = expectedResultRepository;
    }

    @PostMapping("/getDataByDate")
    public List<DataDTO> getDataByDate(@RequestBody DateRequestDTO dateRequest) {
        // 클라이언트에서 전달한 startDate와 endDate를 이용하여 데이터를 조회합니다.
        LocalDateTime startDate = dateRequest.getStartDate();
        LocalDateTime endDate = dateRequest.getEndDate();

        // 조회에 필요한 작업을 수행합니다.
        List<OriResult> oriResults = oriResultRepository.findByModDateBetween(startDate, endDate);

        // 결과를 저장할 리스트를 초기화합니다.
        List<DataDTO> resultData = new ArrayList<>();

        // oriResults의 각 ID를 사용하여 RevResult를 조회하고 개수를 세어줍니다.
        for (OriResult oriResult : oriResults) {
            Long oriResultId = oriResult.getId();
            List<RevResult> revResults = revResultRepository.findByOriResultId(oriResultId);
            int revResultCount = revResults.size();

            // DataDTO 객체를 생성하여 원하는 정보를 추가합니다.
            DataDTO dataDTO = new DataDTO();
            dataDTO.setDate(oriResult.getModDate());
            dataDTO.setComment(oriResult.getComment());
            dataDTO.setId(oriResultId); // OriResult의 ID를 이름으로 지정
            dataDTO.setAmount((double) revResultCount); // RevResult의 개수를 amount로 지정

            // 리스트에 추가합니다.
            resultData.add(dataDTO);
        }

        return resultData;
    }

    @GetMapping("/getDataById/{id}")
    public Map<String, Object> getDataById(@PathVariable Long id) {
        Map<String, Object> resultMap = new HashMap<>();

        // RevResult 조회
        List<RevResult> revResults = revResultRepository.findByOriResultId(id);
        Optional<OriResult> oriResults = oriResultRepository.findById(id);

        resultMap.put("oriResults",oriResults);
        if (!revResults.isEmpty()) {
            resultMap.put("revResults", revResults); // 모든 RevResult를 담는 List로 수정
            // AlloyInput 조회
            List<AlloyInput> alloyInputs = alloyInputRepository.findByRevResultId(id);
            resultMap.put("alloyInputs", alloyInputs);

            // ExpectedResult 조회
            List<ExpectedResult> expectedResults = expectedResultRepository.findByRevResultId(id);
            resultMap.put("expectedResults", expectedResults);
        } else {
            resultMap.put("error", "RevResult not found with ID: " + id);
        }

        return resultMap;
    }

//    @PostMapping("/getDataByDate")
//    public List<DataDTO> getDataByDate(@RequestBody DateRequestDTO dateRequest) {
//        // 클라이언트에서 전달한 startDate와 endDate를 이용하여 데이터를 조회합니다.
//        LocalDateTime startDate = dateRequest.getStartDate();
//        LocalDateTime endDate = dateRequest.getEndDate();
//        System.out.println("----------------------------------");
//        System.out.println(startDate);
//        System.out.println(endDate);
//        System.out.println("----------------------------------");
//
//        // 조회에 필요한 작업을 수행합니다.
//        List<OriResult> oriResults = oriResultRepository.findByModDateBetween(startDate, endDate);
//        List<RevResult> revResults = revResultRepository.findByModDateBetween(startDate, endDate);
//
//        System.out.println("---------------여기-----------------");
//        System.out.println(oriResults);
//        System.out.println(revResults);
//        System.out.println("---------------여기-----------------");
//
//        // 결과를 저장할 리스트를 초기화합니다.
//        List<DataDTO> resultData = new ArrayList<>();
//
//        // OriResult를 기준으로 데이터를 조회합니다.
//        for (OriResult oriResult : oriResults) {
//            Long oriResultId = oriResult.getId();
//            System.out.println("---------------여기1-----------------");
//            System.out.println(oriResultId);
//            System.out.println("---------------여기1-----------------");
//            // OriResult에 해당하는 AlloyInput을 조회합니다.
//            List<AlloyInput> alloyInputs = alloyInputRepository.findByOriResultId(oriResultId);
//            System.out.println("---------------여기2-----------------");
//            System.out.println(alloyInputs);
//            System.out.println("---------------여기2-----------------");
//            // OriResult에 해당하는 ExpectedResult를 조회합니다.
//            List<ExpectedResult> expectedResults = expectedResultRepository.findByOriResultId(oriResultId);
//            System.out.println("---------------여기3-----------------");
//            System.out.println(expectedResults);
//            System.out.println("---------------여기3-----------------");
//            // 데이터를 DataDTO로 변환하고 결과 리스트에 추가합니다.
//            List<DataDTO> dataDTOList = convertToDataDTO(oriResult, null, alloyInputs, expectedResults);
//            System.out.println("---------------여기4-----------------");
//            System.out.println(dataDTOList);
//            System.out.println("---------------여기4-----------------");
//            resultData.addAll(dataDTOList);
//        }
//
//        // RevResult를 기준으로 데이터를 조회합니다.
//        for (RevResult revResult : revResults) {
//            Long revResultId = revResult.getId();
//            // RevResult에 해당하는 AlloyInput을 조회합니다.
//            List<AlloyInput> alloyInputs = alloyInputRepository.findByRevResultId(revResultId);
//
//            // RevResult에 해당하는 ExpectedResult를 조회합니다.
//            List<ExpectedResult> expectedResults = expectedResultRepository.findByRevResultId(revResultId);
//
//            // RevResult에 해당하는 OriResult를 조회합니다.
//            OriResult oriResult = revResult.getOriResult();
//
//            // 데이터를 DataDTO로 변환하고 결과 리스트에 추가합니다.
//            List<DataDTO> dataDTOList = convertToDataDTO(null, revResult, alloyInputs, expectedResults);
//            resultData.addAll(dataDTOList);
//        }
//
//        return resultData;
//    }

    // 데이터를 DTO로 변환하는 메서드 (원하는 형식으로 변환하시면 됩니다)
    private List<DataDTO> convertToDataDTO(OriResult oriResult, RevResult revResult, List<AlloyInput> alloyInputs, List<ExpectedResult> expectedResults) {
        // 데이터를 변환하여 DataDTO 리스트로 생성합니다.
        List<DataDTO> dataDTOList = new ArrayList<>();

        // 예시로 OriResult와 RevResult 데이터를 활용하여 DataDTO를 생성하는 로직을 추가합니다.
        // OriResult와 RevResult의 구체적인 필드와 DataDTO의 필드를 매핑하여 설정해야합니다.
        // 예시로 dataDTO.setName(...)과 dataDTO.setAmount(...)에 적절한 데이터를 설정합니다.
        if (oriResult != null) {
            DataDTO dataDTO = new DataDTO();
            dataDTO.setDate(oriResult.getModDate()); // 예시로 name 필드를 가져옴
            dataDTO.setComment(oriResult.getComment()); // 예시로 amount 필드를 가져옴
            dataDTOList.add(dataDTO);
        }

        if (revResult != null) {
            DataDTO dataDTO = new DataDTO();
            dataDTO.setDate(revResult.getModDate()); // 예시로 name 필드를 가져옴
            dataDTO.setComment(revResult.getComment()); // 예시로 amount 필드를 가져옴
            dataDTOList.add(dataDTO);
        }

        // 예시로 AlloyInput 데이터를 활용하여 DataDTO를 생성하는 로직을 추가합니다.
        for (AlloyInput alloyInput : alloyInputs) {
            DataDTO dataDTO = new DataDTO();
            dataDTO.setName(alloyInput.getName());
            dataDTO.setAmount(alloyInput.getAmount());
            dataDTOList.add(dataDTO);
        }

        // 예시로 ExpectedResult 데이터를 활용하여 DataDTO를 생성하는 로직을 추가합니다.
        for (ExpectedResult expectedResult : expectedResults) {
            DataDTO dataDTO = new DataDTO();
            dataDTO.setName(expectedResult.getName());
            dataDTO.setAmount(expectedResult.getAmount());
            dataDTOList.add(dataDTO);
        }

        // 변환된 DataDTO 리스트를 반환합니다.
        return dataDTOList;
    }
}
