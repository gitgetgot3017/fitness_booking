package com.lhj.fitnessbooking.domain.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInputRequest {

    private MemberCondition condition;
    private MemberGoal goal;
}
