package com.example.demo.dto.base;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultDTO {
    int totalPages;
    int page;
    int pageSize;
    long totalSize;
    List<?> contents;
}
