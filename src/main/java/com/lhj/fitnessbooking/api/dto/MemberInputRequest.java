package com.lhj.FitnessBooking.api.dto;

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
