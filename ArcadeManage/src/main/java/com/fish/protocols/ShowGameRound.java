package com.fish.protocols;

import com.fish.dao.primary.model.GameRound;
import com.fish.dao.primary.model.Rounds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ShowGameRound
{
    private String validTime;

    private String gameName;

    private GameRound gameRound;
    private Rounds rounds;

    public GameRound getGameRound()
    {
        return gameRound;
    }

    public void init()
    {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        this.validTime = format.format(gameRound.getDdstart()) + " - " + format.format(gameRound.getDdend());
    }

    public void setValidTime(String validTime)
    {
        this.validTime = validTime;
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String[] data = validTime.split("-");
        try
        {
            gameRound.setDdstart(format.parse(data[0].trim()));
            gameRound.setDdend(format.parse(data[1].trim()));
        } catch (Exception e)
        {

        }
    }

    public String getValidTime()
    {
        return validTime;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public void setGameRound(GameRound gameRound)
    {
        this.gameRound = gameRound;
    }

    public Rounds getRounds()
    {
        return rounds;
    }

    public void setRounds(Rounds rounds)
    {
        this.rounds = rounds;
    }
}
