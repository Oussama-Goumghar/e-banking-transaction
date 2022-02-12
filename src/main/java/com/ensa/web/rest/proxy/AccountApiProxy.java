package com.ensa.web.rest.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "accountapi")
public interface AccountApiProxy {

    @GetMapping("/api/agences/test")
     String test();
}
