package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.CompareDTO;
import com.seah.specialsteel.dto.DataDTO;
import com.seah.specialsteel.dto.DateRequestDTO;
import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.entity.*;
import com.seah.specialsteel.repository.*;
import org.springframework.http.ResponseEntity;
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
    private final HistoryRepository historyRepository;



    public DataController(OriResultRepository oriResultRepository,
                          RevResultRepository revResultRepository,
                          AlloyInputRepository alloyInputRepository,
                          ExpectedResultRepository expectedResultRepository, List<ResultDTO> oriResultDTOList, HistoryRepository historyRepository) {
        this.oriResultRepository = oriResultRepository;
        this.revResultRepository = revResultRepository;
        this.alloyInputRepository = alloyInputRepository;
        this.expectedResultRepository = expectedResultRepository;
        this.historyRepository = historyRepository;
    }



    @PostMapping("/getDataByDate")
    public List<DataDTO> getDataByDate(@RequestBody DateRequestDTO dateRequest) {
        // 클라이언트에서 전달한 startDate와 endDate를 이용하여 데이터를 조회합니다.
        LocalDateTime startDate = dateRequest.getStartDate();
        LocalDateTime endDate = dateRequest.getEndDate();

        // 조회에 필요한 작업을 수행합니다.
//        List<OriResult> oriResults = oriResultRepository.findByModDateBetween(startDate, endDate);

        List<History> histories = historyRepository.findByDateBetween(startDate, endDate);

        // 결과를 저장할 리스트를 초기화합니다.
        List<DataDTO> resultData = new ArrayList<>();
        List<RevResult> revResults;

        // oriResults의 각 ID를 사용하여 RevResult를 조회하고 개수를 세어줍니다.
        for (History history : histories) {
            Long historyId = history.getId();
            revResults = revResultRepository.findByHistoryId(historyId);
            int revResultCount = revResults.size();

            // DataDTO 객체를 생성하여 원하는 정보를 추가합니다.
            DataDTO dataDTO = new DataDTO();
            dataDTO.setDate(history.getDate());
            dataDTO.setTitle(history.getTitle());
//            dataDTO.setComment(oriResult.getComment());
            dataDTO.setId(historyId); // OriResult의 ID를 이름으로 지정
            dataDTO.setAmount((double) revResultCount); // RevResult의 개수를 amount로 지정

            // 리스트에 추가합니다.
            resultData.add(dataDTO);
        }

        return resultData;
    }

    private List<ResultDTO> oriResultDTOList = new ArrayList<>();
    private List<RevResult> revResults;
    private List<ResultDTO> revResultDTOList = new ArrayList<>();
    private Map<String, Object> resultMap = new HashMap<>();



    //---------------------------------------------------------- 다시 확인해야함
    @GetMapping("/getDataById/{id}")
    public Map<String, Object> getDataById(@PathVariable Long id) {
        // RevResult 조회
        revResults = revResultRepository.findByHistoryId(id);
        Optional<OriResult> oriResults = oriResultRepository.findById(id);

        System.out.println(revResults);
        System.out.println(oriResults);
        System.out.println("리브리설트디티오사이즈 - "+revResultDTOList.size());

        if (!revResults.isEmpty()) {

            for (RevResult revResult : revResults) {
                ResultDTO resultDTO = new ResultDTO();
                resultDTO.id = revResult.getId(); // Assuming there is a getId() method in RevResult class
                resultDTO.totalCost = revResult.getTotalCost(); // Assuming there is a getTotalCost() method in RevResult class
                resultDTO.method = revResult.getMethod();
//                resultDTO.comment = revResult.getComment();
                resultDTO.totalAmount = revResult.getTotalAmount();
                resultDTO.expectOutput = revResult.getExpectOutput();


                // AlloyInput 조회
                List<AlloyInput> alloyInputs = alloyInputRepository.findByRevResultId(revResult.getId());
                Map<String, String> alloyInputsMap = new LinkedHashMap<>();
                for (AlloyInput alloyInput : alloyInputs) {
                    // Assuming there is a getName() method and getValue() method in AlloyInput class
                    alloyInputsMap.put(alloyInput.getName(), String.valueOf(alloyInput.getAmount()));
                }
                resultDTO.alloyInputs = (LinkedHashMap<String, String>) alloyInputsMap;

                // ExpectedResult 조회
                List<ExpectedResult> expectedResults = expectedResultRepository.findByRevResultId(revResult.getId());
                Map<String, String> expectedMaterialsMap = new LinkedHashMap<>();
                for (ExpectedResult expectedResult : expectedResults) {
                    // Assuming there is a getMaterialName() method and getValue() method in ExpectedResult class
                    expectedMaterialsMap.put(expectedResult.getName(), String.valueOf(expectedResult.getAmount()));
                }
                resultDTO.expectMaterials = (LinkedHashMap<String, String>) expectedMaterialsMap;

                revResultDTOList.add(resultDTO);
                System.out.println("리브리썰트 - "+revResultDTOList);
            }
        }

        if (oriResults.isPresent()) {
            System.out.println("오리리썰트이프들어옴");
            ResultDTO oriresultDTO = new ResultDTO();
            oriresultDTO.id = oriResults.get().getId(); // Assuming there is a getId() method in RevResult class
            oriresultDTO.totalCost = oriResults.get().getTotalCost(); // Assuming there is a getTotalCost() method in RevResult class
            oriresultDTO.totalAmount = oriResults.get().getTotalAmount();
//            oriresultDTO.comment = oriResults.get().getComment();
            oriresultDTO.method = oriResults.get().getMethod();
            oriresultDTO.expectOutput = oriResults.get().getExpectOutput();
            System.out.println("오리아이디 - "+oriResults.get().getId());
            // AlloyInput 조회
            List<AlloyInput> alloyInputs = alloyInputRepository.findByOriResultId(oriResults.get().getId());
            Map<String, String> alloyInputsMap = new LinkedHashMap<>();
            for (AlloyInput alloyInput : alloyInputs) {
                // Assuming there is a getName() method and getValue() method in AlloyInput class
                alloyInputsMap.put(alloyInput.getName(), String.valueOf(alloyInput.getAmount()));
            }
            System.out.println("오리얼로이맵 - "+alloyInputsMap);
            oriresultDTO.alloyInputs = (LinkedHashMap<String, String>) alloyInputsMap;

            // ExpectedResult 조회
            List<ExpectedResult> expectedResults = expectedResultRepository.findByOriResultId(oriResults.get().getId());
            Map<String, String> expectedMaterialsMap1 = new LinkedHashMap<>();
            for (ExpectedResult expectedResult : expectedResults) {
                expectedMaterialsMap1.put(expectedResult.getName(), String.valueOf(expectedResult.getAmount()));
            }
            oriresultDTO.expectMaterials = (LinkedHashMap<String, String>) expectedMaterialsMap1;
            System.out.println("오리여기까지");
            oriResultDTOList.add(oriresultDTO);
            System.out.println("오리리설트 - "+oriResultDTOList);
        }


        System.out.println("------------------여기5----------------");
        System.out.println(revResults);
        System.out.println("------------------여기5----------------");
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
        System.out.println("------------------여기6----------------");
        System.out.println(resultMap);
        System.out.println("------------------여기6----------------");
        return resultMap;
    }

    @PostMapping("/getRevData")
    public ResponseEntity<Map<String, Object>> getRevData(@RequestParam ("index")int index) {
        System.out.println("리브리설트디티오사이즈 - " + revResultDTOList.size());
        System.out.println(oriResultDTOList.get(0));
        revResultDTOList.get(index).setLength(revResults.size());
        ResultDTO revResultDTO = revResultDTOList.get(index);
        ResultDTO oriResultDTO = oriResultDTOList.get(0);

        if (oriResultDTOList != null) {

            CompareDTO compareDTO = new CompareDTO(oriResultDTO, revResultDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("revResultDTO", revResultDTO);
            response.put("compareDTO", compareDTO);
            System.out.println("리스폰스 - "+ response);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // 원본
//    @PostMapping("/searchByTitle")
//    public List<DataDTO> searchByTitle(@RequestBody String searchWord){
//        List<OriResult> oriResults = oriResultRepository.findByTitleContaining(searchWord);
//        System.out.println("제목오리결과 - "+oriResults);
//        System.out.println("검색어 - "+searchWord);
//
//        // 결과를 저장할 리스트를 초기화합니다.
//        List<DataDTO> resultData1 = new ArrayList<>();
//        List<RevResult> revResultList;
//
//        // oriResults의 각 ID를 사용하여 RevResult를 조회하고 개수를 세어줍니다.
//        for (OriResult oriResult : oriResults) {
//            Long oriResultId = oriResult.getId();
//            revResultList = revResultRepository.findByOriResultId(oriResultId);
//            int revResultCount = revResultList.size();
//
//            // DataDTO 객체를 생성하여 원하는 정보를 추가합니다.
//            DataDTO dataDTO = new DataDTO();
//            dataDTO.setDate(oriResult.getModDate());
////            dataDTO.setTitle(oriResult.getTitle());
////            dataDTO.setComment(oriResult.getComment());
//            dataDTO.setId(oriResultId); // OriResult의 ID를 이름으로 지정
//            dataDTO.setAmount((double) revResultCount); // RevResult의 개수를 amount로 지정
//
//            // 리스트에 추가합니다.
//            resultData1.add(dataDTO);
//        }
//
//        return resultData1;
//    }


    // 수정
    @PostMapping("/searchByTitle")
    public List<DataDTO> searchByTitle(@RequestBody String searchWord){
        List<History> histories = historyRepository.findByTitleContaining(searchWord);
//                oriResultRepository.findByTitleContaining(searchWord);
        System.out.println("제목오리결과 - "+histories);
        System.out.println("검색어 - "+searchWord);

        // 결과를 저장할 리스트를 초기화합니다.
        List<DataDTO> resultData = new ArrayList<>();
        List<RevResult> revResultList;

        // oriResults의 각 ID를 사용하여 RevResult를 조회하고 개수를 세어줍니다.
        for (History history : histories) {
            Long historyId = history.getId();
            revResultList = revResultRepository.findByHistoryId(historyId);
            int revResultCount = revResultList.size();

            // DataDTO 객체를 생성하여 원하는 정보를 추가합니다.
            DataDTO dataDTO = new DataDTO();
            dataDTO.setDate(history.getDate());
            dataDTO.setTitle(history.getTitle());
//            dataDTO.setComment(oriResult.getComment());
            dataDTO.setId(historyId); // OriResult의 ID를 이름으로 지정
            dataDTO.setAmount((double) revResultCount); // RevResult의 개수를 amount로 지정

            // 리스트에 추가합니다.
            resultData.add(dataDTO);
        }

        return resultData;
    }





}
