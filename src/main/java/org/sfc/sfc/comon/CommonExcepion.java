package org.sfc.sfc.comon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExcepion {

    @ExceptionHandler(RuntimeException.class)
    public Result<?> handle(RuntimeException e){
        return Result.error(e.getMessage());
    }
}

