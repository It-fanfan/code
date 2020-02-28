package com.fish.controller;

import com.fish.dao.primary.model.ShowRanking;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.ProgramRankingService;
import com.fish.service.cache.CacheService;
import com.fish.utils.ExcelUtils;
import com.fish.utils.ExportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class ProgramRankingController
{

    @Autowired
    ProgramRankingService programRankingService;
    @Autowired
    CacheService cacheService;

    //展示小程序比赛结果
    @ResponseBody
    @GetMapping(value = "/programranking")
    public GetResult getGroupRanking(GetParameter parameter)
    {
        return programRankingService.findAll(parameter);
    }

    @GetMapping(value = "/programranking/result")
    public void getProgramRankingResult(ShowRanking ranking, HttpServletResponse response)
    {
        List<ExportResult> rankings = programRankingService.selectResult(ranking);
        String roundName = ranking.getRoundName();
        Date matchdate = ranking.getEndTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String gameTime = format.format(matchdate);
        String gamesName = ranking.getGamesName();
        ExcelUtils.writeExcel(rankings, gameTime + "-" + gamesName + "-" + roundName, response);
    }

}
