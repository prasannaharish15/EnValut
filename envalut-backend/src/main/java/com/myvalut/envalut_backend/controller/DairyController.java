package com.myvalut.envalut_backend.controller;

import com.myvalut.envalut_backend.dto.DairyRequestDTO;
import com.myvalut.envalut_backend.service.DairyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dairy")
public class DairyController {

    private final DairyService dairyService;
    public DairyController(DairyService dairyService){
        this.dairyService=dairyService;
    }


    @PostMapping("/save")
    ResponseEntity<?> saveDairyToDb(@RequestBody DairyRequestDTO dairyRequestDTO, Authentication authentication) throws Exception {
        return dairyService.saveDairyToDb(dairyRequestDTO,authentication);
    }

    @GetMapping("/getdairy")
    ResponseEntity<?> getDairy(Authentication authentication) throws Exception {
        return dairyService.getDairy(authentication);
    }

}
