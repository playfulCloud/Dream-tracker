package com.dreamtracker.app.controller;

import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HabitController {


    private final HabitService habitService;

    @PostMapping("/habits")
    public ResponseEntity<HabitResponse> createHabit(@RequestBody HabitRequest habitRequest){
       return ResponseEntity.ok(habitService.createHabit(habitRequest));
    }




}
