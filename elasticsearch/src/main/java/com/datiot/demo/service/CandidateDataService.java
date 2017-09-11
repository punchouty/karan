package com.datiot.demo.service;

import com.datiot.demo.domain.Candidate;
import com.datiot.demo.domain.CandidateKey;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Service
public class CandidateDataService {

    @Value("${demo.excel-file-path}")
    private String excelFilePath;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private ConstituencyIdCache constituencyIdCache;

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
        Map<String, Integer> constituencyMap = constituencyIdCache.getConstituencyMap();
        Map<CandidateKey, Candidate> candidateMap = readFileUsingPOI();
        for (Candidate candidate : candidateMap.values()) {
            candidate.setConstituencyId(constituencyMap.get(candidate.getConstituency()));
            elasticSearchService.saveDataInElasticSearch(candidate, Candidate.class);
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

    public String getAverageAssetPerConstituency(Integer year) {
        NativeSearchQuery aggregationQuery = new NativeSearchQueryBuilder()
                .withIndices(elasticSearchService.getIndexName(Candidate.class))
                .withTypes(elasticSearchService.getIndexType(Candidate.class))
                .withPageable(new PageRequest(0, 1))
                .withQuery(boolQuery()
                        .must(QueryBuilders.termQuery("year", year)))
                .addAggregation(AggregationBuilders.terms("constituencyId").field("constituencyId")
                        .order(Terms.Order.aggregation("avgAsset", false))
                        .size(60)
                        .subAggregation(AggregationBuilders.avg("avgAsset").field("asset"))
                )
                .build();
        return elasticSearchService.runQuery(aggregationQuery);
    }
}
