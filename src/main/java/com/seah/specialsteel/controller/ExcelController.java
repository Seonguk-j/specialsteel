package com.seah.specialsteel.controller;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.History;
import com.seah.specialsteel.entity.RevResult;
import com.seah.specialsteel.repository.HistoryRepository;
import com.seah.specialsteel.service.ExcelService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;
    private final HistoryRepository historyRepository;
    @GetMapping("/download/{historyId}/{number}")
    public void download(HttpServletResponse res,  @PathVariable Long historyId, @PathVariable String number) throws Exception {


        History history = historyRepository.findById(historyId).orElseThrow();


        /**
         * excel sheet 생성
         */
        Workbook workbook = new XSSFWorkbook();
        excelService.createExcelSheet(workbook, historyId, number);

        /**
         * download
         */

        // 현재 날짜 구하기
        LocalDate todayDate = LocalDate.now();

    // 포맷 형식 지정 (yy년mm월dd일)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy_MM_dd");
        String formattedDate = todayDate.format(formatter);

        String fileName = formattedDate + "_" + history.getTitle();

    // 한글 파일 이름을 URL 인코딩
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        encodedFileName = encodedFileName.replace("+", "%20"); // 공백을 %20으로 변경 (선택사항)

        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");

        ServletOutputStream servletOutputStream = res.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }


}