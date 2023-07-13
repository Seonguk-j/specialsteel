package com.seah.specialsteel.service;

import com.seah.specialsteel.dto.ResultDTO;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HistoryServiceTest {

    @Autowired
    HistoryService historyService;

    @Test
    public void Test() throws IOException, ParseException {
        ResultDTO resultDTO = new ResultDTO("C:\\upload\\2023\\07\\13\\046e98ea-9c81-48a1-9d63-ae0effcfcceb_test2.txt");

//        historyService.saveHistory("C:\\path\\ResponseData_N29790.json", new ResultDTO());

    }





}