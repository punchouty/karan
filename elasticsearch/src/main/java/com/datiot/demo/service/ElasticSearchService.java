package com.datiot.demo.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ElasticSearchService(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }


    public <T> String saveDataInElasticSearch(Object object, Class<T> clazz) {
        String indexName = getIndexName(clazz);
        String generatedId = elasticsearchTemplate.index(getIndexQuery(object, indexName, clazz));
        elasticsearchTemplate.refresh(indexName);
        return generatedId;
    }

    public  <T> String getIndexName(Class<T> clazz) {
        return elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexName();
    }

    public  <T> String getIndexType(Class<T> clazz) {
        return elasticsearchTemplate.getPersistentEntityFor(clazz).getIndexType();
    }

    private <T> IndexQuery getIndexQuery(Object object, String indexName, Class<T> clazz) {
        String type = getIndexType(clazz);
        return new IndexQueryBuilder().withObject(object).withIndexName(indexName).withType(type).build();
    }

    public Aggregations runAggregationQuery(NativeSearchQuery query) {
        return elasticsearchTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
    }

}
