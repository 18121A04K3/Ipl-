package com.saif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static final int MATCH_MATCH_ID = 0;
    public static final int MATCH_SEASON = 1;
    public static final int MATCH_CITY = 2;
    public static final int MATCH_DATE = 3;
    public static final int MATCH_TEAM1 = 4;
    public static final int MATCH_TEAM2 = 5;
    public static final int MATCH_TOSS_WINNER = 6;
    public static final int MATCH_TOSS_DECISION = 7;
    public static final int MATCH_RESULT = 8;
    public static final int MATCH_DL_APPLIED = 9;
    public static final int MATCH_WINNER = 10;
    public static final int MATCH_WIN_BY_RUNS = 11;
    public static final int MATCH_WIN_BY_WICKETS = 12;
    public static final int MATCH_PLAYER_OF_MATCH = 13;
    public static final int MATCH_VENUE = 14;
    public static final int DELIVERY_MATCH_ID = 0;
    public static final int DELIVERY_INNING = 1;
    public static final int DELIVERY_BATTING_TEAM = 2;
    public static final int DELIVERY_BOWLING_TEAM = 3;
    public static final int DELIVERY_OVER = 4;
    public static final int DELIVERY_BALL = 5;
    public static final int DELIVERY_BATSMAN = 6;
    public static final int DELIVERY_NON_STRIKER = 7;
    public static final int DELIVERY_BOWLER = 8;
    public static final int DELIVERY_IS_SUPER_OVER = 9;
    public static final int DELIVERY_WIDE_RUNS = 10;
    public static final int DELIVERY_BYE_RUNS = 11;
    public static final int DELIVERY_LEGBYE_RUNS = 12;
    public static final int DELIVERY_NOBALL_RUNS = 13;
    public static final int DELIVERY_PENALTY_RUNS = 14;
    public static final int DELIVERY_BATSMAN_RUNS = 15;
    public static final int DELIVERY_EXTRA_RUNS = 16;
    public static final int DELIVERY_TOTAL_RUNS = 17;

    public static void main(String[] args) throws Exception {
        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerYear(matches);
        findNumberOfMatchesWonAllTheYears(matches);
        findNumberOfExtraRunsPerTeamIn2016(matches, deliveries);
        findTopEconomicalBowlersIn2015(matches, deliveries);
        findPlayerOfTheMatchesIn2017(matches);
    }

    private static void findPlayerOfTheMatchesIn2017(List<Match> matches) {
        HashMap<String, Integer> playerOfTheMatches = new HashMap<>();
        for (Match matchData : matches) {
            if (matchData.getSeason() == 2017) {
                if (!playerOfTheMatches.containsKey(matchData.getPlayer_of_match())) {
                    playerOfTheMatches.put(matchData.getPlayer_of_match(), 1);
                } else {
                    playerOfTheMatches.put(matchData.getPlayer_of_match(), playerOfTheMatches.get(matchData.getPlayer_of_match()) + 1);
                }
            }
        }
        System.out.println(playerOfTheMatches);
        System.out.println();
    }

    private static void findTopEconomicalBowlersIn2015(List<Match> matches, List<Delivery> deliveries) {
        HashMap<String, Float> bowlersEconomy = new HashMap<>();
        HashMap<String, Integer> numberOfTimesBowled = new HashMap<>();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Match matchRow : matches) {
            if (matchRow.getSeason() == 2015) {
                ids.add(matchRow.getMatch_id());
            }
        }
        for (Delivery deliveryRow : deliveries) {
            if (ids.contains(deliveryRow.getMatch_id())) {
                if (bowlersEconomy.containsKey(deliveryRow.getBowler())) {
                    bowlersEconomy.put(deliveryRow.getBowler(), bowlersEconomy.get(deliveryRow.getBowler()) + deliveryRow.getTotal_runs());
                    numberOfTimesBowled.put(deliveryRow.getBowler(), numberOfTimesBowled.get(deliveryRow.getBowler()) + 1);
                } else {
                    bowlersEconomy.put(deliveryRow.getBowler(), (float) deliveryRow.getTotal_runs());
                    numberOfTimesBowled.put(deliveryRow.getBowler(), 1);
                }
            }
        }
        for (Map.Entry<String, Float> bowlerData : bowlersEconomy.entrySet()) {
            String bowler = bowlerData.getKey();
            float runRate = bowlerData.getValue() / numberOfTimesBowled.get(bowlerData.getKey());
            bowlersEconomy.put(bowler, runRate*6);
        }
        for (int keyValue = 0; keyValue < bowlersEconomy.size(); keyValue++) {
            float minValue = Float.MAX_VALUE;
            String bowler = "";
            for (Map.Entry<String, Float> pairs : bowlersEconomy.entrySet()) {
                if (pairs.getValue() < minValue) {
                    minValue = pairs.getValue();
                    bowler = pairs.getKey();
                }
            }
            System.out.println(bowler + ":" + minValue);
            bowlersEconomy.put(bowler, Float.MAX_VALUE);
        }
    }

    private static void findNumberOfExtraRunsPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        HashMap<Integer, String> idOfTeams = new HashMap<>();
        HashMap<String, Integer> extraRunsPerTeam = new HashMap<>();
        for (Match matchData : matches) {
            if (matchData.getSeason() == 2016) {
                if (matchData.getTeam1().equals(matchData.getWinner())) {
                    idOfTeams.put(matchData.getMatch_id(), matchData.getTeam2());
                } else {
                    idOfTeams.put(matchData.getMatch_id(), matchData.getTeam1());
                }
            }
        }
        for (Map.Entry<Integer, String> teamsId : idOfTeams.entrySet()) {
            int id = teamsId.getKey();
            for (Delivery deliveryRow : deliveries) {
                if (id == deliveryRow.getMatch_id()) {
                    if (!extraRunsPerTeam.containsKey(teamsId.getValue())) {
                        extraRunsPerTeam.put(teamsId.getValue(), deliveryRow.getExtra_runs());
                    } else {
                        extraRunsPerTeam.put(teamsId.getValue(), extraRunsPerTeam.get(teamsId.getValue()) + deliveryRow.getExtra_runs());
                    }
                }
            }
        }
        System.out.println(extraRunsPerTeam);
        System.out.println();
    }

    private static void findNumberOfMatchesWonAllTheYears(List<Match> matches) {
        HashMap<String, Integer> matchesWon = new HashMap<>();
        for (Match matchData : matches) {
            if (!matchesWon.containsKey(matchData.getWinner())) {
                matchesWon.put(matchData.getWinner(), 1);
            } else {
                matchesWon.put(matchData.getWinner(), matchesWon.get(matchData.getWinner()) + 1);
            }
        }
        System.out.println(matchesWon);
        System.out.println();
    }

    private static void findNumberOfMatchesPlayedPerYear(List<Match> matches) {
        int year = 2008;
        int season;
        HashMap<Integer, Integer> matchesPlayedPerYear = new HashMap<>();
        while (year <= 2017) {
            int countYear = 0;
            for (Match matchesData : matches) {
                season = matchesData.getSeason();
                if (season == year) {
                    countYear++;
                }
            }
            matchesPlayedPerYear.put(year, countYear);
            year++;
        }
        System.out.println(matchesPlayedPerYear);
        System.out.println();
    }

    private static List<Delivery> getDeliveriesData() throws Exception {
        BufferedReader scanner = new BufferedReader(new FileReader("/home/shaik/Downloads/IPL_project/deliveries.csv"));
        String line = "";
        int firstRow = 0;
        List<Delivery> deliveries = new ArrayList<>();
        while ((line = scanner.readLine()) != null) {
            if (firstRow == 0) {
                firstRow++;
                continue;
            }
            String[] data = line.split(",");
            Delivery delivery = new Delivery();
            delivery.setMatch_id(Integer.parseInt(data[DELIVERY_MATCH_ID]));
            delivery.setInning(Integer.parseInt(data[DELIVERY_INNING]));
            delivery.setBatting_team(data[DELIVERY_BATTING_TEAM]);
            delivery.setBowling_team(data[DELIVERY_BOWLING_TEAM]);
            delivery.setOver(Integer.parseInt(data[DELIVERY_OVER]));
            delivery.setBall(Integer.parseInt(data[DELIVERY_BALL]));
            delivery.setBatsman(data[DELIVERY_BATSMAN]);
            delivery.setNon_striker(data[DELIVERY_NON_STRIKER]);
            delivery.setBowler(data[DELIVERY_BOWLER]);
            delivery.setIs_super_over(Integer.parseInt(data[DELIVERY_IS_SUPER_OVER]));
            delivery.setWide_runs(Integer.parseInt(data[DELIVERY_WIDE_RUNS]));
            delivery.setBye_runs(Integer.parseInt(data[DELIVERY_BYE_RUNS]));
            delivery.setLegBye_runs(Integer.parseInt(data[DELIVERY_LEGBYE_RUNS]));
            delivery.setNoBall_runs(Integer.parseInt(data[DELIVERY_NOBALL_RUNS]));
            delivery.setPenalty_runs(Integer.parseInt(data[DELIVERY_PENALTY_RUNS]));
            delivery.setBatsman_runs(Integer.parseInt(data[DELIVERY_BATSMAN_RUNS]));
            delivery.setExtra_runs(Integer.parseInt(data[DELIVERY_EXTRA_RUNS]));
            delivery.setTotal_runs(Integer.parseInt(data[DELIVERY_TOTAL_RUNS]));
            deliveries.add(delivery);
        }
        return deliveries;
    }

    private static List<Match> getMatchesData() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("/home/shaik/Downloads/IPL_project/matches.csv"));
        String line = "";
        List<Match> matches = new ArrayList<>();
        int firstRow = 0;
        while ((line = reader.readLine()) != null) {
            if (firstRow == 0) {
                firstRow++;
                continue;
            }
            String[] data = line.split(",");
            Match match = new Match();
            match.setMatch_id(Integer.parseInt(data[MATCH_MATCH_ID]));
            match.setSeason(Integer.parseInt(data[MATCH_SEASON]));
            match.setCity(data[MATCH_CITY]);
            match.setDate(data[MATCH_DATE]);
            match.setTeam1(data[MATCH_TEAM1]);
            match.setTeam2(data[MATCH_TEAM2]);
            match.setToss_winner(data[MATCH_TOSS_WINNER]);
            match.setToss_decision(data[MATCH_TOSS_DECISION]);
            match.setResult(data[MATCH_RESULT]);
            match.setDl_applied(Integer.parseInt(data[MATCH_DL_APPLIED]));
            match.setWinner(data[MATCH_WINNER]);
            match.setWin_by_runs(Integer.parseInt(data[MATCH_WIN_BY_RUNS]));
            match.setWin_by_wickets(Integer.parseInt(data[MATCH_WIN_BY_WICKETS]));
            match.setPlayer_of_match(data[MATCH_PLAYER_OF_MATCH]);
            match.setVenue(data[MATCH_VENUE]);
            matches.add(match);
        }
        return matches;
    }
}
