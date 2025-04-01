package com.example.voting.dto;

import lombok.Data;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;

@Data
public class Voting {

    private BigInteger id;
    private String title;
    private String description;
    private BigInteger startTime;
    private BigInteger endTime;
    private Boolean isActive;

    // Constructor, Getters & Setters
    public Voting(Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean> tuple) {
        this.id = tuple.component1();
        this.title = tuple.component2();
        this.description = tuple.component3();
        this.startTime = tuple.component4();
        this.endTime = tuple.component5();
        this.isActive = tuple.component6();
    }
}
