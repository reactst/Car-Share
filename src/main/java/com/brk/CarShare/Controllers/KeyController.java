package com.brk.CarShare.Controllers;

import com.brk.CarShare.Services.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/key")
@RequiredArgsConstructor
public class KeyController {

    @Autowired
    private final KeyService keyService;
    @GetMapping("/generate")
    public String getKey() {
        return keyService.getKey();
    }
}
