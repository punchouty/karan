package com.datiot.demo.service;

import com.datiot.demo.domain.Candidate;
import com.datiot.demo.domain.Constituency;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ConstituencyDataService {

    private static final Logger log = LoggerFactory.getLogger(ConstituencyDataService.class);
    private String excelFilePath = "";
    @Autowired
    private ConstituencyIdCache constituencyIdCache;
    @Autowired
    private ElasticSearchService elasticSearchService;


    public List<Constituency> saveConstituencyData() throws IOException {
        Map<String, Integer> constituencyMap = constituencyIdCache.getConstituencyMap();
        List<Constituency> constituencyList = readFileUsingPOI();
        for(Constituency constituency : constituencyList) {
            constituency.setConstituencyId(constituencyMap.get(constituency.getConstituencyName()));
            elasticSearchService.saveDataInElasticSearch(constituency, Constituency.class);
        }
        log.info("ConstituencyList: " + constituencyList);
        return constituencyList;
    }

    private List<Constituency> readFileUsingPOI() throws IOException {
        if (excelFilePath == null || "".equals(excelFilePath)) {
            excelFilePath = "/Users/rohitarora/Downloads/Complete State Data_Meghalaya.xlsx";
        }
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = new XSSFWorkbook(inputStream);

        List<Constituency> constituencyList = getConstituencyData(workbook);
        return constituencyList;

    }


    private List<Constituency> getConstituencyData(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> iterator = sheet.iterator();
        List<Constituency> constituencyList = new LinkedList<>();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            // Not creating country object for header
            if (nextRow.getRowNum() == 0)
                continue;


            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Constituency constituency = new Constituency();

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
                        constituency.setConstituencyName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 2:
                        constituency.setDistrictName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 3:
                        constituency.setParliamentConstituencyName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        break;
                    case 4:
                        constituency.setNumberOfVillages(Double.valueOf(cell.getNumericCellValue()).intValue());
                        break;
                    case 14:
                        constituency.setNumberOfMaleVoter18_24(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 15:
                        constituency.setNumberOfMaleVoter25_34(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 16:
                        constituency.setNumberOfMaleVoter35_44(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 17:
                        constituency.setNumberOfMaleVoter45_54(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 18:
                        constituency.setNumberOfMaleVoter55_64(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 19:
                        constituency.setNumberOfMaleVoter65Plus(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 20:
                        constituency.setNumberOfFemaleVoter18_24(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 21:
                        constituency.setNumberOfFemaleVoter25_34(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 22:
                        constituency.setNumberOfFemaleVoter35_44(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 23:
                        constituency.setNumberOfFemaleVoter45_54(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 24:
                        constituency.setNumberOfFemaleVoter55_64(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 25:
                        constituency.setNumberOfFemaleVoter65Plus(Double.valueOf(cell.getNumericCellValue()).longValue());
                        break;
                    case 28:
                        constituency.setNumberOfBooths(Double.valueOf(cell.getNumericCellValue()).intValue());
                        break;
                    case 29:
                        constituency.setConstituencyType(cell.getStringCellValue());
                        break;
                    case 30:
                        constituency.setIncMarginalScore(cell.getNumericCellValue());
                        break;
                    case 31:
                        constituency.setIncDominanceScore(cell.getNumericCellValue());
                        break;
                    case 32:
                        constituency.setIncWinProbability(cell.getNumericCellValue());
                        break;
                    case 33:
                        constituency.setAiScore(cell.getNumericCellValue());
                        break;
                    case 34:
                        constituency.setNmScore(cell.getNumericCellValue());
                        break;
                    case 35:
                        constituency.setIndPartyDominance(cell.getNumericCellValue());
                        break;
                    case 36:
                        constituency.setSocialMediaPenetration(cell.getNumericCellValue());
                        break;
                    default:
                        break;

                }

            }
            constituencyList.add(constituency);

        }
        workbook.close();
        return constituencyList;
    }
}
