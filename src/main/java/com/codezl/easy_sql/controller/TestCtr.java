package com.codezl.easy_sql.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("tst")
public class TestCtr {

    public void tst01() {
        ZoneId zoneId = ZoneId.of("GMT+8");
        //
        LocalDateTime.now(zoneId);
    }
}
