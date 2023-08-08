package com.seah.specialsteel.controller;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.RevResult;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/download/{historyId}")
    public void download(HttpServletResponse res, @PathVariable Long historyId) throws Exception {

        List<Long> revIdList = excelService.revIdList(historyId);

        /**
         * excel sheet 생성
         */
        Workbook workbook = new XSSFWorkbook();
        excelService.createOriSheet(workbook, historyId);

        for(int i = 0; i < revIdList.size(); i++) {
            excelService.createRevSheet(workbook, revIdList.get(i), i + 1);
        }


        /**
         * download
         */
        String fileName = "spring_excel_download";
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = res.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }


}