package com.amway.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrizeDB {
    private String id;
    private String name;
    private Integer total;
    private Integer remaining;

    // 預設得獎權重
    private double weight;
}
