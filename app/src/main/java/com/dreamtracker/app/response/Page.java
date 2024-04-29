package com.dreamtracker.app.response;

import lombok.Data;

import java.util.List;


@Data
public class Page<T> {
    private List<T> items;
}
