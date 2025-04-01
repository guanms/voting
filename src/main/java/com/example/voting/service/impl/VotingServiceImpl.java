package com.example.voting.service.impl;

import com.example.voting.contract.VotingContract;
import com.example.voting.dto.CandidateDto;
import com.example.voting.dto.CreateVotingRequest;
import com.example.voting.dto.VoteRequest;
import com.example.voting.dto.VotingDto;
import com.example.voting.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    private Web3j web3j;

    @Value("${web3j.contract-address}")
    private String contractAddress;

    @Value("${web3j.wallet-private-key}")
    private String walletPrivateKey;

    private final Credentials credentials = Credentials.create(walletPrivateKey);

    public VotingContract loadContract() {
        return VotingContract.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }


    @Override
    public VotingDto createVoting(CreateVotingRequest request) throws Exception {
        VotingContract contract = loadContract();

        List<String> candidateNames = request.getCandidateNames();

        contract.createVoting(
                request.getTitle(),
                request.getDescription(),
                BigIntceger.valueOf(request.getDuration()),
                candidateNames
        ).send();

        return getLatestVoting();

    }

    public VotingDto getLatestVoting() throws Exception {
        VotingContract contract = loadContract();
        BigInteger votingCount = contract.votingCount().send();
        if (votingCount.equals(BigInteger.ZERO)) {
            return null;
        }

        return getVotingById(votingCount.subtract(BigInteger.ONE).longValue());
    }



    @Override
    public VotingDto getVotingById(Long votingId) throws Exception {
        VotingContract contract = loadContract();

        VotingContract.Voting voting = contract.votings(BigInteger.valueOf(votingId)).send();
        List<VotingContract.Candidate> candidates = contract.getVotingResults(BigInteger.valueOf(votingId)).send();

        VotingDto dto = new VotingDto();
        dto.setId(votingId);
        dto.setTitle(voting.title);
        dto.setDescription(voting.description);
        dto.setStartTime(voting.startTime.longValue());
        dto.setEndTime(voting.endTime.longValue());
        dto.setIsActive(voting.isActive);

        List<CandidateDto> candidateDtos = candidates.stream()
                .map(c -> {
                    CandidateDto cd = new CandidateDto();
                    cd.setId(c.id.longValue());
                    cd.setName(c.name);
                    cd.setVoteCount(c.voteCount.longValue());
                    return cd;
                })
                .collect(Collectors.toList());

        dto.setCandidates(candidateDtos);

        return dto;

    }

    @Override
    public List<VotingDto> getActiveVotings() throws Exception {
        VotingContract contract = loadContract();

        List<VotingContract.Voting> votings = contract.getActiveVotings().send();

        return votings.stream()
                .map(v -> {
                    VotingDto dto = new VotingDto();
                    dto.setId(v.id.longValue());
                    dto.setTitle(v.title);
                    dto.setDescription(v.description);
                    dto.setStartTime(v.startTime.longValue());
                    dto.setEndTime(v.endTime.longValue());
                    dto.setIsActive(v.isActive);
                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void vote(VoteRequest request) throws Exception {
        VotingContract contract = loadContract();

        contract.vote(
                BigInteger.valueOf(request.getVotingId()),
                BigInteger.valueOf(request.getCandidateId())
        ).send();

    }
}
