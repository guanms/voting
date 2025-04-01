package com.example.voting.service;

import com.example.voting.dto.CreateVotingRequest;
import com.example.voting.dto.VoteRequest;
import com.example.voting.dto.VotingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.List;

@Service
public class VotingServiceImpl implements VotingService{

    @Autowired
    private Web3j web3j;

    @Value("${web3j.wallet-private-key}")
    private String walletPrivateKey;

    private final Credentials credentials = Credentials.create(walletPrivateKey);


    @Override
    public VotingDto createVoting(CreateVotingRequest request) throws Exception {
        return null;
    }

    @Override
    public VotingDto getVotingById(Long votingId) throws Exception {
        return null;
    }

    @Override
    public List<VotingDto> getActiveVotings() throws Exception {
        return List.of();
    }

    @Override
    public void vote(VoteRequest request) throws Exception {

    }
}
