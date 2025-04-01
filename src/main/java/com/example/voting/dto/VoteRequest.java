package com.example.voting.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private Long votingId;
    private Long candidateId;
    private String voterAddress;
}
