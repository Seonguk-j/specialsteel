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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelService {

    private final OriResultRepository oriResultRepository;

    private final RevResultRepository revResultRepository;

    private final AlloyInputRepository alloyInputRepository;

    private final ExpectedResultRepository expectedResultRepository;

    private List<String> colName() {

        List<String> colName = new ArrayList<>();
        colName.add("Heat 번호");
        colName.add("알고리즘(기존/수정)");
        colName.add("합금철 총 투입비용");
        colName.add("합금철 총 투입량");
        colName.add("예상용강량");
        colName.add("방법");


//        colName.add("메모");

        return colName;
    }

    private List<AlloyInput> alloyData(Long oriResultId, Long revResultId) {
        List<AlloyInput> alloyInputList = new ArrayList<>();
        if(oriResultId!= null) {
            OriResult oriResult = oriResultRepository.findById(oriResultId).orElseThrow();
             alloyInputList = alloyInputRepository.findByOriResultId(oriResultId);


            return alloyInputList;
        }else {
            RevResult revResult = revResultRepository.findById(revResultId).orElseThrow();
            alloyInputList = alloyInputRepository.findByRevResultId(revResultId);

            return alloyInputList;
        }
    }

    private List<ExpectedResult> expectData(Long orResultId, Long revResultId) {
        List<ExpectedResult> expectedResultList = new ArrayList<>();
        if(orResultId!= null) {
            OriResult oriResult = oriResultRepository.findById(orResultId).orElseThrow();
            expectedResultList = expectedResultRepository.findByOriResultId(orResultId);


            return expectedResultList;
        }else {
            RevResult revResult = revResultRepository.findById(revResultId).orElseThrow();
            expectedResultList = expectedResultRepository.findByRevResultId(revResultId);

            return expectedResultList;
        }
    }

    public Sheet createExcelSheet(Workbook workbook, Long historyId) {

        List<OriResult> OriResultList = oriResultRepository.findByHistoryId(historyId);
        List<RevResult> revResultList = revResultRepository.findByHistoryId(historyId);
        Sheet sheet = workbook.createSheet("알고리즘"); // 엑셀 sheet 이름


        List<String> oriRowName = this.colName();

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

        List<AlloyInput> alloyData2 = this.alloyData(OriResultList.get(0).getId(), null);
        List<ExpectedResult> expectData2 = this.expectData(OriResultList.get(0).getId(), null);


        Row expectNameRow = sheet.createRow(0);
        Cell expectNameCell = expectNameRow.createCell(oriRowName.size());
        expectNameCell.setCellValue("예상 성분");

        Cell alloyNameCell = expectNameRow.createCell(oriRowName.size() + expectData2.size());
        alloyNameCell.setCellValue("합금철 별 투입량");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, oriRowName.size(),oriRowName.size() + expectData2.size()-1));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, oriRowName.size() + expectData2.size(),oriRowName.size() + expectData2.size() + alloyData2.size() - 1));




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

        for (int j = 0; j < alloyData2.size(); j++) {
            Cell bodyCell = headerRow2.createCell(j + 6 + expectData2.size());
            bodyCell.setCellValue(alloyData2.get(j).getName()); // 데이터 추가

        }


        Cell alloyBodyCell = null;
        for (int i = 0; i < OriResultList.size(); i++) {

            List<AlloyInput> alloyData = this.alloyData(OriResultList.get(i).getId(), null);
            List<ExpectedResult> expectData = this.expectData(OriResultList.get(i).getId(), null);

            List<AlloyInput> revAlloyData = this.alloyData(null, revResultList.get(i).getId());
            List<ExpectedResult> revExpectData = this.expectData(null, revResultList.get(i).getId());


            headerRow = sheet.createRow((i + 1) * 2);
            //히트 번호 셀
            heatCell = headerRow.createCell(0);
            heatCell.setCellValue("heatNo" + i);

            //알고리즘 타입(기존, 수정) 셀
            algorithmCell = headerRow.createCell(1);
            algorithmCell.setCellValue("기존");

            totalCost = headerRow.createCell(2);

            //합금철 총 투입량
            totalAmount = headerRow.createCell(3);

            //예상 용강량 셀
            expectOutputCell = headerRow.createCell(4);


            methodCell = headerRow.createCell(5);


            // 기존 알고리즘인 경우
            totalCost.setCellValue(OriResultList.get(i).getTotalCost());
            totalAmount.setCellValue(OriResultList.get(i).getTotalAmount());
            methodCell.setCellValue(OriResultList.get(i).getMethod());
            expectOutputCell.setCellValue(Double.toString(OriResultList.get(i).getExpectOutput()));

            for (int j = 0; j < expectData.size(); j++) {
                expectBody = headerRow.createCell(j + 6);
                expectBody.setCellValue(Double.toString(expectData.get(j).getAmount())); // 데이터 추가
            }

            for (int j = 0; j < alloyData.size(); j++) {
                alloyBodyCell = headerRow.createCell(j + 6 + expectData.size());
                alloyBodyCell.setCellValue(Double.toString(alloyData.get(j).getAmount())); // 데이터 추가
            }

            headerRow3 = sheet.createRow((i + 1) * 2 + 1);
            //히트 번호 셀
            heatCell = headerRow3.createCell(0);
            heatCell.setCellValue("heatNo" + i);

            //알고리즘 타입(기존, 수정) 셀
            algorithmCell = headerRow3.createCell(1);
            algorithmCell.setCellValue("수정");

            totalCost = headerRow3.createCell(2);

            //합금철 총 투입량
            totalAmount = headerRow3.createCell(3);

            //예상 용강량 셀
            expectOutputCell = headerRow3.createCell(4);


            methodCell = headerRow3.createCell(5);

            // 수정 알고리즘인 경우
            totalCost.setCellValue(revResultList.get(i).getTotalCost());
            totalAmount.setCellValue(revResultList.get(i).getTotalAmount());
            expectOutputCell.setCellValue(Double.toString(revResultList.get(i).getExpectOutput()));
            methodCell.setCellValue(revResultList.get(i).getMethod());
            for (int j = 0; j < revExpectData.size(); j++) {
                expectBody = headerRow3.createCell(j + 6);
                expectBody.setCellValue(Double.toString(revExpectData.get(j).getAmount())); // 데이터 추가
            }

            for (int j = 0; j < revAlloyData.size(); j++) {
                alloyBodyCell = headerRow3.createCell(j + 6 + revExpectData.size());
                alloyBodyCell.setCellValue(Double.toString(revAlloyData.get(j).getAmount())); // 데이터 추가
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

