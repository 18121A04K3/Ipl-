package com.saif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    public static void main(String[] args) throws Exception{
        List<Match> matches=getMatchesData();
        List<Delivery> deliveries=getDeliveriesData();
        getNumberOfMatchesPlayedPerYear(matches);
        getNumberOfMatchesWonAllTheYears(matches);
        getNumberOfExtraRunsIn2016(matches,deliveries);
        getTopEconomicalBowlersIn2015(matches,deliveries);
        getPlayerOfTheMatchesIn2017(matches);

    }

    private static void getPlayerOfTheMatchesIn2017(List<Match> matches) {
        HashMap<String,Integer> player=new HashMap<>();
        for(Match var:matches)
        {
            if(var.getSeason()==2017)
            {
                if(!player.containsKey(var.getPlayer_of_match()))
                {
                    player.put(var.getPlayer_of_match(),1);
                }
                else {
                    player.put(var.getPlayer_of_match(),player.get(var.getPlayer_of_match())+1);
                }
            }
        }
        System.out.println(player);
    }

    private static void getTopEconomicalBowlersIn2015(List<Match> matches, List<Delivery> deliveries) {
        HashMap<String,Integer> actualRuns=new HashMap<>();
        HashMap<String,Integer> noTimes=new HashMap<>();
        ArrayList<Integer> allId=new ArrayList<>();
        for(Match itr:matches)
        {
            if(itr.getSeason()==2015)
            {
                allId.add(itr.getMatch_id());
            }
        }
        for(Delivery scan:deliveries)
        {
            if(allId.contains(scan.getMatch_id()))
            {
                if(actualRuns.containsKey(scan.getBowler()))
                {
                    actualRuns.put(scan.getBowler(), actualRuns.get(scan.getBowler()) + scan.getTotal_runs());
                    noTimes.put(scan.getBowler(), noTimes.get(scan.getBowler()) + 1);
                }
                else{
                    actualRuns.put(scan.getBowler(), scan.getTotal_runs());
                    noTimes.put(scan.getBowler(),1);
                }
            }
        }
        for(Map.Entry<String,Integer> item: actualRuns.entrySet())
        {
            String bowler= item.getKey();
            float runRate=(float)item.getValue()/noTimes.get(item.getKey());
            System.out.println(bowler+" = "+runRate);
        }

    }

    private static void getNumberOfExtraRunsIn2016(List<Match> matches, List<Delivery> deliveries) {
        HashMap<Integer, String> ids = new HashMap<>();
        HashMap<String, Integer> extraRunsPerTeam = new HashMap<>();
        for (Match val : matches) {
            if(val.getSeason()==2016) {
                if (val.getTeam1().equals(val.getWinner())) {
                    ids.put(val.getMatch_id(), val.getTeam2());
                } else {
                    ids.put(val.getMatch_id(), val.getTeam1());
                }
            }
        }
        for (Map.Entry<Integer, String> value : ids.entrySet())
        {
            int id= value.getKey();
            for (Delivery var : deliveries)
            {
                if (id==var.getMatch_id())
                {
                    if(!extraRunsPerTeam.containsKey(value.getValue()))
                    {
                        extraRunsPerTeam.put(value.getValue(),var.getExtra_runs());
                    }
                    else{
                        extraRunsPerTeam.put(value.getValue(), extraRunsPerTeam.get(value.getValue())+ var.getExtra_runs());
                    }
                }
        }
    }
        System.out.println(extraRunsPerTeam);
    }

    private static void getNumberOfMatchesWonAllTheYears(List<Match> matches) {
        HashMap<String,Integer> matchesWon=new HashMap<>();
        for(Match row:matches){
            if(!matchesWon.containsKey(row.getWinner())){
                matchesWon.put(row.getWinner(),1);
            }
            else{
                matchesWon.put(row.getWinner(),matchesWon.get(row.getWinner())+1);
            }
        }
        System.out.println(matchesWon);
    }

    private static void getNumberOfMatchesPlayedPerYear(List<Match> matches) {
        int year=2008;
        int k;
        HashMap<Integer,Integer> dict=new HashMap<>();
        while(year<=2017)
        {
            int count=0;
            for(Match val:matches)
            {
                k=val.getSeason();
                if(k==year){
                    count++;
                }
            }
            dict.put(year,count);
            year++;
        }
        System.out.println(dict);
    }


    private static List<Delivery> getDeliveriesData() throws Exception{
        BufferedReader scanner=new BufferedReader(new FileReader("/home/shaik/Downloads/IPL_project/deliveries.csv"));
        String line = "";
        int i = 0;
        List<Delivery> deliveries=new ArrayList<>();
        while((line=scanner.readLine())!=null){
            if(i==0)
            {
                i++;
                continue;
            }
            String[] data=line.split(",");
            Delivery delivery=new Delivery();
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

    private static List<Match> getMatchesData() throws Exception{
        BufferedReader reader=new BufferedReader(new FileReader("/home/shaik/Downloads/IPL_project/matches.csv"));
        String line = "";
        List<Match> matches=new ArrayList<>();
        int i=0;
        while((line=reader.readLine() )!= null){
            if(i==0){
                i++;
                continue;
            }
            String[] data=line.split(",");
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
