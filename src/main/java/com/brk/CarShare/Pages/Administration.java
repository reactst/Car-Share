package com.brk.CarShare.Pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Administration {
    @GetMapping("/administration/")
    public String GetIndex(){
        return "administration/index";
    }
}
