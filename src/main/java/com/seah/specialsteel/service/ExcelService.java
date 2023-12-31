package com.seah.specialsteel.service;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.ExpectedResultRepository;
import com.seah.specialsteel.repository.OriResultRepository;
import com.seah.specialsteel.repository.RevResultRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ExcelService {

    private final OriResultRepository oriResultRepository;

    private final RevResultRepository revResultRepository;

    private final AlloyInputRepository alloyInputRepository;

    private final ExpectedResultRepository expectedResultRepository;

    static HashSet<String> alloyNameSet;

    private HashMap<String, Double> alloyData(Long oriResultId, Long revResultId) {
        HashMap<String, Double> alloyInputMap = new HashMap<>();
        if(oriResultId!= null) {
            List<AlloyInput> alloyInputs = alloyInputRepository.findByOriResultId(oriResultId);
            for(AlloyInput alloyInput : alloyInputs)
                alloyInputMap.put(alloyInput.getName(), alloyInput.getAmount());

            return alloyInputMap;
        }else {
            List<AlloyInput> alloyInputs = alloyInputRepository.findByRevResultId(revResultId);
            for(AlloyInput alloyInput : alloyInputs)
                alloyInputMap.put(alloyInput.getName(), alloyInput.getAmount());

            return alloyInputMap;
        }
    }

    private List<ExpectedResult> expectData(Long oriResultId, Long revResultId) {
        List<ExpectedResult> expectedResultList = new ArrayList<>();
        if(oriResultId!= null) {
            expectedResultList = expectedResultRepository.findByOriResultId(oriResultId);
            return expectedResultList;
        }else {
            expectedResultList = expectedResultRepository.findByRevResultId(revResultId);
            return expectedResultList;
        }
    }

    public Sheet createExcelSheet(Workbook workbook, Long historyId, String number) {
        List<OriResult> OriResultList = oriResultRepository.findByHistoryId(historyId);
        List<RevResult> revResultList = revResultRepository.findByHistoryId(historyId);

        alloyNameSet = new HashSet<>();
        for(OriResult oriResult : OriResultList) {
            List<AlloyInput> alloyInputs = alloyInputRepository.findByOriResultId(oriResult.getId());
            for(AlloyInput alloyInput : alloyInputs) {
                alloyNameSet.add(alloyInput.getName());
            }
        }

        ArrayList<String> alloyNameList = new ArrayList<>(alloyNameSet);

        Sheet sheet = workbook.createSheet("알고리즘"); // 엑셀 sheet 이름

        List<String> oriRowName = new ArrayList<>();

        String[] colName = {"Heat 번호","알고리즘(기존/수정)","합금철 총 투입비용", "합금철 총 투입량","예상용강량","방법"};
        HashMap<String, Integer> colNameMap = new HashMap<>();
        for(int i =0; i < colName.length; i++){
            int n = number.charAt(i) - '1';
            oriRowName.add(colName[n]);
            colNameMap.put(colName[n], i);
        }

        Row headerRow;
        Row headerRow3;
        Cell headerCell;
        Cell heatCell;
        Cell algorithmCell;

        Cell totalAmount;

        Cell totalCost;

        //예상 용강량
        Cell expectOutputCell;
        //방법
        Cell methodCell;

        Cell expectBody;

        int rowCount = 1;

        Row headerRow2 = sheet.createRow(rowCount);

        for (int i = 0; i < oriRowName.size(); i++) {
            headerCell = headerRow2.createCell(i);
            headerCell.setCellValue(oriRowName.get(i)); // 데이터 추가

        }

        List<ExpectedResult> expectData2 = this.expectData(OriResultList.get(0).getId(), null);


        Row expectNameRow = sheet.createRow(0);
        Cell expectNameCell = expectNameRow.createCell(oriRowName.size());
        expectNameCell.setCellValue("예상 성분");

        Cell alloyNameCell = expectNameRow.createCell(oriRowName.size() + expectData2.size());
        alloyNameCell.setCellValue("합금철 별 투입량");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, oriRowName.size(),oriRowName.size() + expectData2.size()-1));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, oriRowName.size() + expectData2.size(),oriRowName.size() + expectData2.size() + alloyNameList.size() - 1));

        CellStyle centeredStyle = workbook.createCellStyle();
        // 배경색 설정
        centeredStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        centeredStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //폰트 설정
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        centeredStyle.setFont(font);
        // 예상 성분 셀에 스타일 적용
        expectNameCell.setCellStyle(centeredStyle);

        // 합금철 별 투입량 셀에 스타일 적용
        alloyNameCell.setCellStyle(centeredStyle);


        for (int j = 0; j < expectData2.size(); j++) {
            Cell bodyCell = headerRow2.createCell(j + 6);
            bodyCell.setCellValue(expectData2.get(j).getName()); // 데이터 추가

        }

        for (int j = 0; j < alloyNameList.size(); j++) {
            Cell bodyCell = headerRow2.createCell(j + 6 + expectData2.size());
            bodyCell.setCellValue(alloyNameList.get(j)); // 데이터 추가
        }


        Cell alloyBodyCell = null;
        for (int i = 0; i < OriResultList.size(); i++) {

            HashMap<String, Double> alloyData = this.alloyData(OriResultList.get(i).getId(), null);
            List<ExpectedResult> expectData = this.expectData(OriResultList.get(i).getId(), null);

            HashMap<String, Double> revAlloyData = this.alloyData(null, revResultList.get(i).getId());
            List<ExpectedResult> revExpectData = this.expectData(null, revResultList.get(i).getId());


            headerRow = sheet.createRow((i + 1) * 2);
            //히트 번호 셀
            heatCell = headerRow.createCell(colNameMap.get("Heat 번호"));
            heatCell.setCellValue(OriResultList.get(i).getHeatNo());

            //알고리즘 타입(기존, 수정) 셀
            algorithmCell = headerRow.createCell(colNameMap.get("알고리즘(기존/수정)"));
            algorithmCell.setCellValue("기존");

            totalCost = headerRow.createCell(colNameMap.get("합금철 총 투입비용"));

            //합금철 총 투입량
            totalAmount = headerRow.createCell(colNameMap.get("합금철 총 투입량"));

            //예상 용강량 셀
            expectOutputCell = headerRow.createCell(colNameMap.get("예상용강량"));


            methodCell = headerRow.createCell(colNameMap.get("방법"));


            // 기존 알고리즘인 경우
            totalCost.setCellValue(OriResultList.get(i).getTotalCost());
            totalAmount.setCellValue(OriResultList.get(i).getTotalAmount());
            methodCell.setCellValue(OriResultList.get(i).getMethod());
            expectOutputCell.setCellValue(Double.toString(OriResultList.get(i).getExpectOutput()));

            for (int j = 0; j < expectData.size(); j++) {
                expectBody = headerRow.createCell(j + 6);
                expectBody.setCellValue(Double.toString(expectData.get(j).getAmount())); // 데이터 추가
            }


            // 데이터 값 등록
            for (int j = 0; j < alloyNameList.size(); j++) {
                alloyBodyCell = headerRow.createCell(j + 6 + expectData.size());
                alloyBodyCell.setCellValue(Double.toString(alloyData.getOrDefault(alloyNameList.get(j), 0.0))); // 데이터 추가
            }

            headerRow3 = sheet.createRow((i + 1) * 2 + 1);
            //히트 번호 셀
            heatCell = headerRow3.createCell(colNameMap.get("Heat 번호"));
            heatCell.setCellValue(revResultList.get(i).getHeatNo());

            //알고리즘 타입(기존, 수정) 셀
            algorithmCell = headerRow3.createCell(colNameMap.get("알고리즘(기존/수정)"));
            algorithmCell.setCellValue("수정");

            totalCost = headerRow3.createCell(colNameMap.get("합금철 총 투입비용"));

            //합금철 총 투입량
            totalAmount = headerRow3.createCell(colNameMap.get("합금철 총 투입량"));

            //예상 용강량 셀
            expectOutputCell = headerRow3.createCell(colNameMap.get("예상용강량"));


            methodCell = headerRow3.createCell(colNameMap.get("방법"));

            // 수정 알고리즘인 경우
            totalCost.setCellValue(revResultList.get(i).getTotalCost());
            totalAmount.setCellValue(revResultList.get(i).getTotalAmount());
            expectOutputCell.setCellValue(Double.toString(revResultList.get(i).getExpectOutput()));
            methodCell.setCellValue(revResultList.get(i).getMethod());
            for (int j = 0; j < revExpectData.size(); j++) {
                expectBody = headerRow3.createCell(j + 6);
                expectBody.setCellValue(Double.toString(revExpectData.get(j).getAmount())); // 데이터 추가
            }

            for (int j = 0; j < alloyNameList.size(); j++) {
                alloyBodyCell = headerRow3.createCell(j + 6 + revExpectData.size());
                alloyBodyCell.setCellValue(Double.toString(revAlloyData.getOrDefault(alloyNameList.get(j), 0.0))); // 데이터 추가
            }

        }
        int totalColumns = 0;

// 모든 행에 대해
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                // 해당 행의 열 수를 확인하고 이전 최대값과 비교하여 업데이트
                totalColumns = Math.max(totalColumns, row.getLastCellNum());
            }
        }

        for (int i = 0; i < totalColumns; i++) {
            sheet.autoSizeColumn(i);
        }

        return sheet;
    }

    // 특정 셀에 특정 값 넣기
    private void setValue(Sheet sheet, String position, String value) {
        CellReference ref = new CellReference(position);
        Row r = sheet.getRow(ref.getRow());

        if (r == null) {
            // 행이 존재하지 않으면 새로운 행 생성
            r = sheet.createRow(ref.getRow());
        }

        Cell c = r.getCell(ref.getCol());

        if (c == null) {
            // 셀이 존재하지 않으면 새로운 셀 생성
            c = r.createCell(ref.getCol());
        }

        c.setCellValue(value);
    }

}

