package com.datiot.demo.service;

import com.datiot.demo.domain.Constituency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ConstituencyDataServiceTest {

    @Autowired
    private ConstituencyDataService constituencyDataService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before() {
        if (elasticsearchTemplate.indexExists(Constituency.class)) {
            elasticsearchTemplate.deleteIndex(Constituency.class);
        }
        elasticsearchTemplate.createIndex(Constituency.class);
        elasticsearchTemplate.putMapping(Constituency.class);
        elasticsearchTemplate.refresh(Constituency.class);
    }

    @Test
    public void shouldSaveConstituencyData() throws IOException {
        List<Constituency> constituencyList = constituencyDataService.saveConstituencyData();
        for (Constituency constituency : constituencyList) {
            assertThat(constituency.getId(), is(notNullValue()));
        }
    }

}