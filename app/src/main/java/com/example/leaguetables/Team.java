package com.example.leaguetables;

public class Team {
    private String name;
    private String league;
    private int points, goalfor, goalagaisnt, goaldiff;

    public Team(String name, String league, int points, int goalagaisnt, int  goaldiff, int goalfor){
        this.name = name;
        this.league = league;
        this.points = points;
        this.goalfor = goalfor;
        this.goalagaisnt = goalagaisnt;
        this.goaldiff = goaldiff;
    }


    public int getGoalagaisnt() {
        return goalagaisnt;
    }

    public int getGoaldiff() {
        return goaldiff;
    }

    public int getGoalfor() {
        return goalfor;
    }

    public String getLeague() {
        return league;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
