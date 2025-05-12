package com.nsmm.esg.csddservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EuddAnswerRequest {
    private Map<String, Boolean> answers; // key: questionId, value: 예(true)/아니오(false)
}