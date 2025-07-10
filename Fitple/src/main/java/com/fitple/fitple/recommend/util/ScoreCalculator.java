package com.fitple.fitple.recommend.util;

public class ScoreCalculator {

    // 채용 점수: 평균보다 얼마나 높으냐에 따라 0~10점
    public static int calculateJobScore(int salary, double averageSalary) {
        int diff = salary - (int) averageSalary;

        if (diff >= 1000) return 10;
        else if (diff >= 500) return 7;
        else if (diff >= 0) return 5;
        else if (diff >= -500) return 2;
        else return 0;
    }

    // 주거 점수: 보증금, 월세 각각 10점 만점 → 총 20점
    public static int calculateHousingScore(int deposit, int rent, double avgDeposit, double avgRent) {
        int depositScore = 0;
        int rentScore = 0;

        double depositDiff = avgDeposit - deposit;
        double rentDiff = avgRent - rent;

        // 보증금 낮으면 10점, 비슷하면 5점, 높으면 0점
        if (depositDiff > 5000) depositScore = 10;
        else if (depositDiff > 1000) depositScore = 7;
        else if (depositDiff >= 0) depositScore = 5;
        else if (depositDiff >= -1000) depositScore = 2;

        // 월세 낮으면 10점, 비슷하면 5점, 높으면 0점
        if (rentDiff > 20) rentScore = 10;
        else if (rentDiff > 10) rentScore = 7;
        else if (rentDiff >= 0) rentScore = 5;
        else if (rentDiff >= -10) rentScore = 2;

        return depositScore + rentScore; // 최대 20점
    }
}
