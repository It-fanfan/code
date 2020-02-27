package servlet.match;

import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;

/**
 * 赛区奖励，进行主动查询中奖纪录,并发送奖励
 */
@WebServlet(urlPatterns = "/reward/query", name = "reward")
public class CmServletMatchReward extends CmServletMain
{
}
