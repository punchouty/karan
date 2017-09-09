package com.datiot.demo;

import com.datiot.demo.domain.Candidate;
import com.datiot.demo.service.CandidateDataService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner init(CandidateDataService candidateDataService, ElasticsearchTemplate elasticsearchTemplate) {
        return (evt) -> {
            if (elasticsearchTemplate.indexExists(Candidate.class)) {
                elasticsearchTemplate.deleteIndex(Candidate.class);
                elasticsearchTemplate.deleteIndex(".kibana");
            }
            elasticsearchTemplate.createIndex(Candidate.class);
            elasticsearchTemplate.putMapping(Candidate.class);
            elasticsearchTemplate.refresh(Candidate.class);

            candidateDataService.saveCandidateData();
        };
    }
}
