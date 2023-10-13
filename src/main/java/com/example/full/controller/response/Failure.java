package com.example.full.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Failure implements Result {
    private String msg;
}
