package com.example.voting.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateVotingRequest {
    private String title;
    private String description;
    private Long duration; // in seconds
    private List<String> candidateNames;
}
