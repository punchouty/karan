package com.datiot.demo.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class JsonTest {

    @Test
    public void shouldLoadJson() {
        String filePath = "/Users/rohitarora/Downloads/meghalaya.json";
        Map<Integer, String> constituencyMap = new HashMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            String jsonString = IOUtils.toString(inputStream);
            ObjectMapper mapper = new ObjectMapper();
            Map data = mapper.readValue(jsonString, Map.class);
            System.out.println(data);
            List<Map> dataList = ((List<Map>)((Map)((Map)data.get("objects")).get("meghalayap")).get("geometries"));
            for(Map dataMap : dataList) {
                String name = (String) ((Map)(Map)dataMap.get("properties")).get("AC_NAME");
                Integer id = (Integer) ((Map)(Map)dataMap.get("properties")).get("AC_NO");
                name = name.replace("(ST)", "").trim();
                constituencyMap.put(id, name);
            }
            for(Map.Entry<Integer, String> entry : constituencyMap.entrySet()) {
                System.out.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            fail("Should not reach here");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
