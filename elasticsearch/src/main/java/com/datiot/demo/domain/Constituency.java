package com.datiot.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "candidate", type = "candidate")
public class Constituency {

    @Id
    private String id;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer constituencyId;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String constituencyName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ParliamentConstituencyName;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer parliamentConstituencyId;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer numberOfVillages;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer numberOfBooths;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter18_24;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter25_34;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter35_44;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter45_54;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter55_64;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfMaleVoter65Plus;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter18_24;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter25_34;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter35_44;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter45_54;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter55_64;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long numberOfFemaleVoter65Plus;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String constituencyType;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double incMarginalScore;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double incDominanceScore;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double incWinProbability;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double aiScore;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double nmScore;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double socialMediaPenetration;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double indPartyDominance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getConstituencyId() {
        return constituencyId;
    }

    public void setConstituencyId(Integer constituencyId) {
        this.constituencyId = constituencyId;
    }

    public Double getIndPartyDominance() {
        return indPartyDominance;
    }

    public void setIndPartyDominance(Double indPartyDominance) {
        this.indPartyDominance = indPartyDominance;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    public void setConstituencyName(String constituencyName) {
        this.constituencyName = constituencyName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getParliamentConstituencyName() {
        return ParliamentConstituencyName;
    }

    public void setParliamentConstituencyName(String parliamentConstituencyName) {
        ParliamentConstituencyName = parliamentConstituencyName;
    }

    public Integer getParliamentConstituencyId() {
        return parliamentConstituencyId;
    }

    public void setParliamentConstituencyId(Integer parliamentConstituencyId) {
        this.parliamentConstituencyId = parliamentConstituencyId;
    }

    public Integer getNumberOfVillages() {
        return numberOfVillages;
    }

    public void setNumberOfVillages(Integer numberOfVillages) {
        this.numberOfVillages = numberOfVillages;
    }

    public Integer getNumberOfBooths() {
        return numberOfBooths;
    }

    public void setNumberOfBooths(Integer numberOfBooths) {
        this.numberOfBooths = numberOfBooths;
    }

    public Long getNumberOfMaleVoter18_24() {
        return numberOfMaleVoter18_24;
    }

    public void setNumberOfMaleVoter18_24(Long numberOfMaleVoter18_24) {
        this.numberOfMaleVoter18_24 = numberOfMaleVoter18_24;
    }

    public Long getNumberOfMaleVoter25_34() {
        return numberOfMaleVoter25_34;
    }

    public void setNumberOfMaleVoter25_34(Long numberOfMaleVoter25_34) {
        this.numberOfMaleVoter25_34 = numberOfMaleVoter25_34;
    }

    public Long getNumberOfMaleVoter35_44() {
        return numberOfMaleVoter35_44;
    }

    public void setNumberOfMaleVoter35_44(Long numberOfMaleVoter35_44) {
        this.numberOfMaleVoter35_44 = numberOfMaleVoter35_44;
    }

    public Long getNumberOfMaleVoter45_54() {
        return numberOfMaleVoter45_54;
    }

    public void setNumberOfMaleVoter45_54(Long numberOfMaleVoter45_54) {
        this.numberOfMaleVoter45_54 = numberOfMaleVoter45_54;
    }

    public Long getNumberOfMaleVoter55_64() {
        return numberOfMaleVoter55_64;
    }

    public void setNumberOfMaleVoter55_64(Long numberOfMaleVoter55_64) {
        this.numberOfMaleVoter55_64 = numberOfMaleVoter55_64;
    }

    public Long getNumberOfMaleVoter65Plus() {
        return numberOfMaleVoter65Plus;
    }

    public void setNumberOfMaleVoter65Plus(Long numberOfMaleVoter65Plus) {
        this.numberOfMaleVoter65Plus = numberOfMaleVoter65Plus;
    }

    public Long getNumberOfFemaleVoter18_24() {
        return numberOfFemaleVoter18_24;
    }

    public void setNumberOfFemaleVoter18_24(Long numberOfFemaleVoter18_24) {
        this.numberOfFemaleVoter18_24 = numberOfFemaleVoter18_24;
    }

    public Long getNumberOfFemaleVoter25_34() {
        return numberOfFemaleVoter25_34;
    }

    public void setNumberOfFemaleVoter25_34(Long numberOfFemaleVoter25_34) {
        this.numberOfFemaleVoter25_34 = numberOfFemaleVoter25_34;
    }

    public Long getNumberOfFemaleVoter35_44() {
        return numberOfFemaleVoter35_44;
    }

    public void setNumberOfFemaleVoter35_44(Long numberOfFemaleVoter35_44) {
        this.numberOfFemaleVoter35_44 = numberOfFemaleVoter35_44;
    }

    public Long getNumberOfFemaleVoter45_54() {
        return numberOfFemaleVoter45_54;
    }

    public void setNumberOfFemaleVoter45_54(Long numberOfFemaleVoter45_54) {
        this.numberOfFemaleVoter45_54 = numberOfFemaleVoter45_54;
    }

    public Long getNumberOfFemaleVoter55_64() {
        return numberOfFemaleVoter55_64;
    }

    public void setNumberOfFemaleVoter55_64(Long numberOfFemaleVoter55_64) {
        this.numberOfFemaleVoter55_64 = numberOfFemaleVoter55_64;
    }

    public Long getNumberOfFemaleVoter65Plus() {
        return numberOfFemaleVoter65Plus;
    }

    public void setNumberOfFemaleVoter65Plus(Long numberOfFemaleVoter65Plus) {
        this.numberOfFemaleVoter65Plus = numberOfFemaleVoter65Plus;
    }

    public String getConstituencyType() {
        return constituencyType;
    }

    public void setConstituencyType(String constituencyType) {
        this.constituencyType = constituencyType;
    }

    public Double getIncMarginalScore() {
        return incMarginalScore;
    }

    public void setIncMarginalScore(Double incMarginalScore) {
        this.incMarginalScore = incMarginalScore;
    }

    public Double getIncDominanceScore() {
        return incDominanceScore;
    }

    public void setIncDominanceScore(Double incDominanceScore) {
        this.incDominanceScore = incDominanceScore;
    }

    public Double getIncWinProbability() {
        return incWinProbability;
    }

    public void setIncWinProbability(Double incWinProbability) {
        this.incWinProbability = incWinProbability;
    }

    public Double getAiScore() {
        return aiScore;
    }

    public void setAiScore(Double aiScore) {
        this.aiScore = aiScore;
    }

    public Double getNmScore() {
        return nmScore;
    }

    public void setNmScore(Double nmScore) {
        this.nmScore = nmScore;
    }

    public Double getSocialMediaPenetration() {
        return socialMediaPenetration;
    }

    public void setSocialMediaPenetration(Double socialMediaPenetration) {
        this.socialMediaPenetration = socialMediaPenetration;
    }
}
