package com.seah.specialsteel.service;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.ExpectedResultRepository;
import com.seah.specialsteel.repository.OriResultRepository;
import com.seah.specialsteel.repository.RevResultRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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
        colName.add("제목");
        colName.add("합금철 총 투입비용");
        colName.add("합금철 총 투입량");
        colName.add("예상 용강량");
        colName.add("방법");
        colName.add("메모");

        return colName;
    }

    private List<String> oriData(Long orResultId) {
        OriResult oriResult = oriResultRepository.getOne(1L);
        List<String> oriData = new ArrayList<>();
        oriData.add(oriResult.getTitle());
        oriData.add(Double.toString(oriResult.getTotalCost()));
        oriData.add(Double.toString(oriResult.getTotalAmount()));
        oriData.add(Double.toString(oriResult.getExpectOutput()));
        oriData.add(oriResult.getMethod());
        oriData.add(oriResult.getComment());

        return oriData;
    }

    private List<AlloyInput> alloyData(Long orResultId) {
        OriResult oriResult = oriResultRepository.getOne(1L);
        List<AlloyInput> alloyInputList = alloyInputRepository.findByOriResult(oriResult);


        return alloyInputList;
    }

    private List<ExpectedResult> expectData(Long orResultId) {
        OriResult oriResult = oriResultRepository.getOne(1L);
        List<ExpectedResult> expectedResultList = expectedResultRepository.findByOriResult(oriResult);


        return expectedResultList;
    }

    public Sheet createOriSheet(Workbook workbook) {

        Sheet sheet = workbook.createSheet("기존 알고리즘"); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

        // F 컬럼의 너비를 35로 설정
//        int columnIndexF = 4; // F 컬럼은 0부터 시작하여 5번째 컬럼.
//        sheet.setColumnWidth(columnIndexF, 100);

        /**
         * header font style
         */
        XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
        headerXSSFFont.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null));

        /**
         * header cell style
         */
        XSSFCellStyle headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
        headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{34, 37, 41}, null));

        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerXssfCellStyle.setFont(headerXSSFFont);

        /**
         * body cell style
         */
//        XSSFCellStyle bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();
//
//        // 테두리 설정
//        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        /**
         * header data
         */
        //A1 값 넣기
        this.setValue(sheet, "A1", "기존 알고리즘 결과");

        int rowCount = 2; // 데이터가 저장될 행
        List<String> oriRowName = this.colName();


        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < oriRowName.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(oriRowName.get(i)); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }

        /**
         * body data
         */
        List<String> oriBodyData = this.oriData(1L);
        Row bodyRow = null;
        Cell bodyCell = null;

        bodyRow = sheet.createRow(rowCount++);
        for (int i = 0; i < oriBodyData.size(); i++) {
            bodyCell = bodyRow.createCell(i);
            bodyCell.setCellValue(oriBodyData.get(i)); // 데이터 추가
//                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
        }


        Row alloyHeaderRow = sheet.createRow(6);
        Cell alloyBodyName = null;

        Row alloyBodyRow = sheet.createRow(7);
        Cell alloyBodyValue = null;

        List<AlloyInput> alloyData = this.alloyData(1L);
        for (int i = 0; i < alloyData.size(); i++) {
            alloyBodyName = alloyHeaderRow.createCell(i);
            alloyBodyName.setCellValue(alloyData.get(i).getName());
            alloyBodyValue = alloyBodyRow.createCell(i);
            alloyBodyValue.setCellValue(alloyData.get(i).getAmount());
        }

        Row expectHeaderRow = sheet.createRow(10);
        Cell expectBodyName = null;

        Row expectBodyRow = sheet.createRow(11);
        Cell expectBodyValue = null;

        List<ExpectedResult> expectData = this.expectData(1L);
        for (int i = 0; i < expectData.size(); i++) {
            expectBodyName = expectHeaderRow.createCell(i);
            expectBodyName.setCellValue(expectData.get(i).getName());
            expectBodyValue = expectBodyRow.createCell(i);
            expectBodyValue.setCellValue(expectData.get(i).getAmount());
        }
        return sheet;
    }
    public Sheet createRevSheet(Workbook workbook) {

        Sheet sheet = workbook.createSheet("수정 알고리즘"); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

        // F 컬럼의 너비를 35로 설정
//        int columnIndexF = 4; // F 컬럼은 0부터 시작하여 5번째 컬럼.
//        sheet.setColumnWidth(columnIndexF, 100);

        /**
         * header font style
         */
        XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
        headerXSSFFont.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null));

        /**
         * header cell style
         */
        XSSFCellStyle headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
        headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{34, 37, 41}, null));

        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerXssfCellStyle.setFont(headerXSSFFont);

        /**
         * body cell style
         */
//        XSSFCellStyle bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();
//
//        // 테두리 설정
//        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
//        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        /**
         * header data
         */
        //A1 값 넣기
        this.setValue(sheet, "A1", "수정 알고리즘 결과");

        int rowCount = 2; // 데이터가 저장될 행
        List<String> revRowName = this.oriColName(1L);


        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < oriRowName.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(oriRowName.get(i)); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }

        /**
         * body data
         */
        List<String> oriBodyData = this.oriData(1L);
        Row bodyRow = null;
        Cell bodyCell = null;

        bodyRow = sheet.createRow(rowCount++);
        for (int i = 0; i < oriBodyData.size(); i++) {
            bodyCell = bodyRow.createCell(i);
            bodyCell.setCellValue(oriBodyData.get(i)); // 데이터 추가
//                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
        }


        Row alloyHeaderRow = sheet.createRow(6);
        Cell alloyBodyName = null;

        Row alloyBodyRow = sheet.createRow(7);
        Cell alloyBodyValue = null;

        List<AlloyInput> alloyData = this.alloyData(1L);
        for (int i = 0; i < alloyData.size(); i++) {
            alloyBodyName = alloyHeaderRow.createCell(i);
            alloyBodyName.setCellValue(alloyData.get(i).getName());
            alloyBodyValue = alloyBodyRow.createCell(i);
            alloyBodyValue.setCellValue(alloyData.get(i).getAmount());
        }

        Row expectHeaderRow = sheet.createRow(10);
        Cell expectBodyName = null;

        Row expectBodyRow = sheet.createRow(11);
        Cell expectBodyValue = null;

        List<ExpectedResult> expectData = this.expectData(1L);
        for (int i = 0; i < expectData.size(); i++) {
            expectBodyName = expectHeaderRow.createCell(i);
            expectBodyName.setCellValue(expectData.get(i).getName());
            expectBodyValue = expectBodyRow.createCell(i);
            expectBodyValue.setCellValue(expectData.get(i).getAmount());
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

