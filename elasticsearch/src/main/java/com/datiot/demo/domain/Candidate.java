package com.datiot.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "candidate", type = "candidate")
public class Candidate {

    @Id
    private String id;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String candidateName;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private int year;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String constituency;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private int age;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String education;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String party;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String votePercentage;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long asset;
    @Field(type = FieldType.Boolean, index = FieldIndex.not_analyzed)
    private boolean criminalCharges;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String district;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String position;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long totalValidVotes;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer constituencyId;

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getVotePercentage() {
        return votePercentage;
    }

    public void setVotePercentage(String votePercentage) {
        this.votePercentage = votePercentage;
    }

    public Long getAsset() {
        return asset;
    }

    public void setAsset(Long asset) {
        this.asset = asset;
    }

    public boolean isCriminalCharges() {
        return criminalCharges;
    }

    public void setCriminalCharges(boolean criminalCharges) {
        this.criminalCharges = criminalCharges;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getTotalValidVotes() {
        return totalValidVotes;
    }

    public void setTotalValidVotes(Long totalValidVotes) {
        this.totalValidVotes = totalValidVotes;
    }

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
}
