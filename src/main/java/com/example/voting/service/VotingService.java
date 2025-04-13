package com.example.voting.service;

import com.example.voting.dto.CreateVotingRequest;
import com.example.voting.dto.VoteRequest;
import com.example.voting.dto.VotingDto;

import java.util.List;

public interface VotingService {

    VotingDto createVoting(CreateVotingRequest request);

    VotingDto getVotingById(Long votingId) ;

    List<VotingDto> getActiveVotings() ;

    void vote(VoteRequest request) ;

}
