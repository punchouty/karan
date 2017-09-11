package com.datiot.demo;

import com.datiot.demo.domain.Candidate;
import com.datiot.demo.domain.Constituency;
import com.datiot.demo.service.CandidateDataService;
import com.datiot.demo.service.ConstituencyDataService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    //@Profile("dev")
    CommandLineRunner init(CandidateDataService candidateDataService, ElasticsearchTemplate elasticsearchTemplate, ConstituencyDataService constituencyDataService) {
        return (evt) -> {
            if (elasticsearchTemplate.indexExists(Candidate.class)) {
                elasticsearchTemplate.deleteIndex(Candidate.class);
                //elasticsearchTemplate.deleteIndex(".kibana");
            }
            if (elasticsearchTemplate.indexExists(Constituency.class)) {
                elasticsearchTemplate.deleteIndex(Constituency.class);
            }
            elasticsearchTemplate.createIndex(Candidate.class);
            elasticsearchTemplate.putMapping(Candidate.class);
            elasticsearchTemplate.refresh(Candidate.class);
            elasticsearchTemplate.createIndex(Constituency.class);
            elasticsearchTemplate.putMapping(Constituency.class);
            elasticsearchTemplate.refresh(Constituency.class);

            candidateDataService.saveCandidateData();
            constituencyDataService.saveConstituencyData();
        };
    }
}
