package com.lhj.fitnessbooking.api.dto;

public enum MemberGoal {
    FLEXIBILITY("유연성 향상"),
    WEIGHT("체중 감량"),
    STRENGTH("체력 증진"),
    MIND("정신 수련");

    private String explanation;

    MemberGoal(String explanation) {
        this.explanation = explanation;
    }


    @Override
    public String toString() {
        return explanation;
    }
}
