package com.example.voting.service;

import com.example.voting.dto.CreateVotingRequest;
import com.example.voting.dto.VoteRequest;
import com.example.voting.dto.VotingDto;

import java.util.List;

public interface VotingService {

    VotingDto createVoting(CreateVotingRequest request) throws Exception;
    VotingDto getVotingById(Long votingId) throws Exception;
    List<VotingDto> getActiveVotings() throws Exception;
    void vote(VoteRequest request) throws Exception;

}
