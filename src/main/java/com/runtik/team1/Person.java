package com.runtik.team1;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private Long account;
    private String name;
    private Double value;
}
