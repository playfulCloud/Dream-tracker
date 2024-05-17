package com.dreamtracker.app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class Page<T> {
    private List<T> items;
}
