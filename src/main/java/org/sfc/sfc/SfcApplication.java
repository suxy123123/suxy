package org.sfc.sfc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.sfc.sfc.mapper")
public class SfcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfcApplication.class, args);
    }

}
