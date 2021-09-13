package com.example.mymiaosha;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class MymiaoshaApplicationTests {

    @Test
    void contextLoads() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        System.out.println(dateFormat.format(date));
    }

}
