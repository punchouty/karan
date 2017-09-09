package com.datiot.demo.service;

import com.datiot.demo.domain.Candidate;
import com.datiot.demo.domain.CandidateKey;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Service
public class CandidateDataService {

    private static final Logger log = LoggerFactory.getLogger(CandidateDataService.class);

    @Value("${demo.excel-file-path}")
    private String excelFilePath;
    @Value("${demo.json-file-path}")
    private String jsonFilePath;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private Map<String, Integer> constituencyMap;


    private Map<CandidateKey, Candidate> readFileUsingPOI() throws IOException {
        if (excelFilePath == null) {
            excelFilePath = "/Users/rohitarora/Downloads/Candidate_Cleaned_Data_Final.xlsx";
        }
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);

        Map<CandidateKey, Candidate> candidateMap = getCandidateInfo(workbook);
        getCandidateAsset(workbook, candidateMap);
        return candidateMap;

    }

    public void saveCandidateData() throws IOException {
        initalizeConstituencyMap();
        Map<CandidateKey, Candidate> candidateMap = readFileUsingPOI();
        for (Candidate candidate : candidateMap.values()) {
            candidate.setConstituencyId(constituencyMap.get(candidate.getConstituency()));
            elasticSearchService.saveDataInElasticSearch(candidate, Candidate.class);
        }
    }

    private void initalizeConstituencyMap() {
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
    }

    private Map<CandidateKey, Candidate> getCandidateInfo(Workbook workbook) throws IOException {
        Map<CandidateKey, Candidate> candidateMap = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            // Not creating country object for header
            if (nextRow.getRowNum() == 0)
                continue;


            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Candidate candidate = new Candidate();
            CandidateKey candidateKey = new CandidateKey();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                /*if (cell.getCellTypeEnum() == CellType.STRING) {
                    System.out.print(cell.getStringCellValue() + "--");
                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    System.out.print(cell.getNumericCellValue() + "--");
                }*/

                int columnIndex = cell.getColumnIndex();
                switch (columnIndex + 1) {
                    case 1:
                        candidate.setYear(Double.valueOf(cell.getNumericCellValue()).intValue());
                        candidateKey.setYear(candidate.getYear());
                        break;
                    case 2:
                        candidate.setParty(cell.getStringCellValue().toUpperCase());
                        break;
                    case 3:
                        break;
                    case 4:
                        candidate.setConstituency(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 5:
                        candidate.setDistrict(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 6:
                        candidate.setCandidateName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        candidateKey.setCandidateName(candidate.getCandidateName());
                        break;
                    case 7:
                        candidate.setTotalValidVotes(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 8:
                        candidate.setPosition(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case 9:
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            candidate.setVotePercentage(cell.getStringCellValue());
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            candidate.setVotePercentage(String.valueOf(cell.getNumericCellValue()));
                        }
                        break;
                    default:
                        break;

                }

            }
            candidateMap.put(candidateKey, candidate);

        }
        workbook.close();
        return candidateMap;
    }

    private Map<CandidateKey, Candidate> getCandidateAsset(Workbook workbook, Map<CandidateKey, Candidate> candidateMap) throws IOException {
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            // Not creating country object for header
            if (nextRow.getRowNum() == 0)
                continue;


            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Candidate candidate = new Candidate();
            CandidateKey candidateKey = new CandidateKey();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                /*if (cell.getCellTypeEnum() == CellType.STRING) {
                    System.out.print(cell.getStringCellValue() + "--");
                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    System.out.print(cell.getNumericCellValue() + "--");
                }*/

                int columnIndex = cell.getColumnIndex();
                switch (columnIndex + 1) {
                    case 1:
                        candidate.setCandidateName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        candidateKey.setCandidateName(candidate.getCandidateName());
                        break;
                    case 2:
                        candidate.setYear(Double.valueOf(cell.getNumericCellValue()).intValue());
                        candidateKey.setYear(candidate.getYear());
                        break;
                    case 3:
                        candidate.setConstituency(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 4:
                        candidate.setAge(Double.valueOf(cell.getNumericCellValue()).intValue());
                        break;
                    case 5:
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            candidate.setEducation(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            candidate.setEducation(String.valueOf(cell.getNumericCellValue()));
                        }
                        break;
                    case 6:
                        candidate.setParty(cell.getStringCellValue().toUpperCase());
                        break;
                    case 8:
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            candidate.setVotePercentage(cell.getStringCellValue());
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            candidate.setVotePercentage(String.valueOf(cell.getNumericCellValue()));
                        }
                        break;
                    case 9:
                        candidate.setCriminalCharges("Y".equalsIgnoreCase(cell.getStringCellValue()) ? true : false);
                        break;
                    case 11:
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            String value = cell.getStringCellValue().trim().replace("\u00A0", "");
                            candidate.setAsset(new Long(value));
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            candidate.setAsset(Double.valueOf(cell.getNumericCellValue()).longValue());
                        }


                        break;
                    default:
                        break;

                }

            }
            Candidate existingCandidate = candidateMap.get(candidateKey);
            if (existingCandidate != null) {
                BeanUtils.copyProperties(candidate, existingCandidate);
            }

        }
        workbook.close();
        return candidateMap;
    }

    public void getAverage(Integer year) {
        NativeSearchQuery aggregationQuery = new NativeSearchQueryBuilder()
                .withIndices(elasticSearchService.getIndexName(Candidate.class))
                .withTypes(elasticSearchService.getIndexType(Candidate.class))
                .withQuery(boolQuery()
                        .must(QueryBuilders.termQuery("year", year)))
                .addAggregation(AggregationBuilders.terms("constituencyId").field("constituencyId")
                        .subAggregation(AggregationBuilders.avg("avgAsset").field("asset"))
                )
                .build();
        Aggregations aggregations = elasticSearchService.runAggregationQuery(aggregationQuery);
        System.out.println(aggregations);
    }
}
