package com.datiot.demo.service;

import com.datiot.demo.domain.Candidate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CandidateDataServiceTest {

    @Autowired
    CandidateDataService candidateDataService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before() {
        elasticsearchTemplate.deleteIndex(Candidate.class);
        elasticsearchTemplate.refresh(Candidate.class);
    }
    @Test
    public void shouldSaveCandidateData() {
        try {
            candidateDataService.saveCandidateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





