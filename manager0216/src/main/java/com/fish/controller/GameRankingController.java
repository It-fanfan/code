package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGames;
import com.fish.dao.primary.model.Ranking;
import com.fish.dao.primary.model.RankingRecord;
import com.fish.dao.primary.model.ShowRanking;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.RankingService;
import com.fish.service.cache.CacheService;
import com.fish.utils.ExcelUtils;
import com.fish.utils.ExportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/manage")
public class GameRankingController
{

    @Autowired
    RankingService rankingService;
    @Autowired
    CacheService cacheService;
    //展示比赛结果
    @GetMapping(value = "/ranking")
    public GetResult getRanking(GetParameter parameter)
    {
        return rankingService.findAll(parameter);
    }
    //导出Excel结果
    @GetMapping(value = "/ranking/result")
    public void getRankingResult(ShowRanking ranking, HttpServletResponse response)
    {
        List<ExportResult> rankings = rankingService.selectResult(ranking);
        String roundName = ranking.getRoundName();
        Date matchdate = ranking.getMatchdate();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = format.format(matchdate);
        Integer gamesCode  = ranking.getGamesCode();
        ArcadeGames games = cacheService.getGames(gamesCode);
        String gameName = games.getDdname();
        ExcelUtils.writeExcel(rankings,gameTime+"-"+gameName+"-"+roundName, response);
    }

}
