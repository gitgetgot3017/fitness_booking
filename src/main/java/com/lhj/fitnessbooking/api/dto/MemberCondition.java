package com.lhj.fitnessbooking.api.dto;

public enum MemberCondition {
    GOOD("좋아요"),
    SOSO("보통이에요"),
    BAD("나빠요");

    private String explanation;

    MemberCondition(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return explanation;
    }
}
