package com.example.voting.service.impl;

import com.example.voting.contract.VotingContract;
import com.example.voting.dto.*;
import com.example.voting.service.VotingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotingServiceImpl implements VotingService {

    private static final Logger logger = LoggerFactory.getLogger(VotingServiceImpl.class);

    @Autowired
    private Web3j web3j;

    private static String contractAddress;

    @Value("${web3j.wallet-private-key}")
    private String walletPrivateKey;

    private Credentials credentials;

    @PostConstruct // 添加初始化方法
    public void init() {
        if (walletPrivateKey == null || walletPrivateKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet private key cannot be null or empty");
        }
        this.credentials = Credentials.create(walletPrivateKey);
    }

    static {
        try {
            Path configPath = Paths.get("contract.properties");
            if (Files.exists(configPath)) {
                Properties properties = new Properties();
                properties.load(Files.newInputStream(configPath));
                contractAddress = properties.getProperty("contract.address");
                logger.info("Loaded contract address: {}", contractAddress);
            }
        }catch (Exception e){
            logger.error("Failed to load contract address from properties file", e);
        }
    }

    public VotingContract loadContract() {
        return VotingContract.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider() {
                    @Override
                    public BigInteger getGasLimit(String contractFunc) {
                        return BigInteger.valueOf(5_000_000L); // 更高的gas limit
                    }
                }
        );
    }


    @Override
    public VotingDto createVoting(CreateVotingRequest request)  {
        logger.info("Creating voting with request: {}", request);
        try {
            VotingContract contract = loadContract();
            logger.info("contract address:{}", contract.getContractAddress());
            logger.info("候选人组：{}", request.getCandidateNames());
            TransactionReceipt receipt = contract.createVoting(
                    request.getTitle(),
                    request.getDescription(),
                    BigInteger.valueOf(request.getDuration()),
                    request.getCandidateNames()
            ).send();

            // 等待交易确认
            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Transaction failed: " + receipt.getStatus());
            }
            logger.info("Transaction receipt: {}", receipt.isStatusOK());

            Thread.sleep(3000); // 1秒延迟
            BigInteger votingCount = contract.votingCount().send();
            logger.info("Create Voting count: {}", votingCount);

            List<VotingContract.VotingCreatedEventResponse> events = VotingContract.getVotingCreatedEvents(receipt);
            if (!events.isEmpty()) {
                BigInteger votingId = events.get(0).votingId;
                logger.info("Voting created with ID: {}", votingId);
            }


            logger.info("Voting created successfully");
            return getLatestVoting();
        } catch (Exception e) {
            logger.error("Error creating voting", e);
            throw new RuntimeException("Failed to create voting", e);
        }

    }

    public VotingDto getLatestVoting() throws IOException {
        VotingContract contract = loadContract();
        try {

            BigInteger votingCount = contract.votingCount().send();
            logger.info("Voting count: {}", votingCount);

            if (votingCount == null || votingCount.equals(BigInteger.ZERO)) {
                logger.warn("No voting exists yet");
                return null;
            }

            return getVotingById(votingCount.subtract(BigInteger.ONE).longValue());
        } catch (Exception e) {
            logger.error("合约调用失败，请检查：");
            logger.error("1. 合约地址 {} 是否正确", contract.getContractAddress());
            logger.error("2. votingCount 方法是否存在：{}", contract.getContractBinary());
            logger.error("3. 节点 RPC 是否正常：{}", web3j.web3ClientVersion().send().getWeb3ClientVersion());
        }
        return null;
    }



    @Override
    public VotingDto getVotingById(Long votingId)  {
        VotingDto dto = new VotingDto();
        try {
            VotingContract contract = loadContract();

            Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger> votingTuple =
                    contract.getVotingDetails(BigInteger.valueOf(votingId)).send();
            Voting voting = new Voting(votingTuple);  // 转换为 Voting 对象
            List<VotingContract.Candidate> candidates = contract.getVotingResults(BigInteger.valueOf(votingId)).send();


            dto.setId(votingId);
            dto.setTitle(voting.getTitle());
            dto.setDescription(voting.getDescription());
            dto.setStartTime(voting.getStartTime().longValue());
            dto.setEndTime(voting.getEndTime().longValue());
            dto.setIsActive(voting.getIsActive());

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
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;

    }

    @Override
    public List<VotingDto> getActiveVotings() {
        List<VotingDto> votingDtoList = new ArrayList<>();
        try {
            VotingContract contract = loadContract();

            Tuple3<List<BigInteger>, List<String>, List<BigInteger>> activeVotings = contract.getActiveVotings().send();

            // 转换逻辑
            for (int i = 0; i < activeVotings.component1().size(); i++) {
                VotingDto dto = new VotingDto();
                dto.setId(activeVotings.component1().get(i).longValue());
                dto.setTitle(activeVotings.component2().get(i));
                dto.setEndTime(activeVotings.component3().get(i).longValue());
                votingDtoList.add(dto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return votingDtoList;

    }

    @Override
    public void vote(VoteRequest request)  {
        try {
            VotingContract contract = loadContract();

            contract.vote(
                    BigInteger.valueOf(request.getVotingId()),
                    BigInteger.valueOf(request.getCandidateId())
            ).send();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
