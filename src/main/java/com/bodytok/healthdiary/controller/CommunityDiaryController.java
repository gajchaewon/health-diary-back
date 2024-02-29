package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.service.CommunityExerciseDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
public class CommunityDiaryController {
    private final CommunityExerciseDiaryService communityDiaryService;

}
