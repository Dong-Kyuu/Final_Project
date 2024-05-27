package com.example.jhta_3team_finalproject.cache;

import lombok.Getter;

@Getter
public enum CacheType {

    INQUERY_BOARD_PAGE("inqueryBoardPage", 200, 100);

    private final String cacheName;
    private final int secsToExpireAfterWrite;
    private final int entryMaxSize;

    CacheType(String cacheName, int secsToExpireAfterWrite, int entryMaxSize) {
        this.cacheName = cacheName;
        this.secsToExpireAfterWrite = secsToExpireAfterWrite;
        this.entryMaxSize = entryMaxSize;
    }
}
