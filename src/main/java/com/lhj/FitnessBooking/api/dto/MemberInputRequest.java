package com.lhj.FitnessBooking.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInputRequest {

    private MemberCondition condition;
    private MemberGoal goal;
}
