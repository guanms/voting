package com.example.voting.dto;

import lombok.Data;

import java.util.List;

@Data
public class VotingDto {
    private Long id;
    private String title;
    private String description;
    private Long startTime;
    private Long endTime;
    private Boolean isActive;
    private List<CandidateDto> candidates;

}
