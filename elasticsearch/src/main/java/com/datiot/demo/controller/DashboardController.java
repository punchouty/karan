package com.datiot.demo.controller;

import com.datiot.demo.service.CandidateDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CandidateDataService candidateDataService;

    @RequestMapping(value = "candidateAverageAsset/{year}/", method = RequestMethod.GET)
    public String getCandidateAverageAsset(@PathVariable Integer year) {
        candidateDataService.getAverage(year);
        return "";
    }
}
