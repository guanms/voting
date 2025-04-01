package com.example.voting.dto;

import lombok.Data;

@Data
public class CandidateDto {
    private Long id;
    private String name;
    private Long voteCount;
}
