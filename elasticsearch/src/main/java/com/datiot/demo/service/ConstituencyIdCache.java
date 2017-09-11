package com.datiot.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConstituencyIdCache {

    private static final Logger log = LoggerFactory.getLogger(ConstituencyDataService.class);
    private String jsonFilePath = null;
    private Map<String, Integer> constituencyMap;

    public Map<String, Integer> getConstituencyMap() {
        if (constituencyMap == null || constituencyMap.entrySet().size() == 0) {
            constituencyMap = new HashMap<>();
            if (jsonFilePath == null || "".equals(jsonFilePath)) {
                jsonFilePath = "/Users/rohitarora/Downloads/meghalaya.json";
            }
            try {
                FileInputStream inputStream = new FileInputStream(new File(jsonFilePath));
                String jsonString = IOUtils.toString(inputStream);
                ObjectMapper mapper = new ObjectMapper();
                Map data = mapper.readValue(jsonString, Map.class);
                List<Map> dataList = ((List<Map>) ((Map) ((Map) data.get("objects")).get("meghalayap")).get("geometries"));
                for (Map dataMap : dataList) {
                    String name = (String) ((Map) (Map) dataMap.get("properties")).get("AC_NAME");
                    Integer id = (Integer) ((Map) (Map) dataMap.get("properties")).get("AC_NO");
                    name = name.replace("(ST)", "").trim();
                    constituencyMap.put(name, id);
                }

            } catch (IOException e) {
                log.error("Something went wrong while reading json: ", e);
            }
        }
        return constituencyMap;
    }
}
