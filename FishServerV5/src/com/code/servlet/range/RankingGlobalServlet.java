package com.code.servlet.range;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.ranking.RankingGlobalProtocol;
import com.code.protocols.social.SocialBase;
import com.code.service.range.BookRankingService;
import com.code.servlet.base.ServletMain;
import com.utils.XwhTool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.code.protocols.ranking.RankingGlobalProtocol.RequestImpl;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/range/global", name = "com.code.protocols.ranking.RankingGlobalProtocol")
public class RankingGlobalServlet extends ServletMain<RequestImpl, RankingGlobalProtocol.ResponseImpl>
{
    @Override
    protected RankingGlobalProtocol.ResponseImpl doLogic(RequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        UserCache userCache = getUserCache(request);
        RankingGlobalProtocol.ResponseImpl res = new RankingGlobalProtocol.ResponseImpl();
        SocialBase.putResponse(SocialBase.ERROR.SUCCESS, res);
        BookRankingService service = BookRankingService.getInstance();
        res.globals = BigData.getBigData(service.globalRanking(), XwhTool.getDeclaredFieldsAll(RankingGlobalProtocol.RankingUser.class));
        res.rankglobal = service.getMyRanking(userCache);
        return res;
    }
}
