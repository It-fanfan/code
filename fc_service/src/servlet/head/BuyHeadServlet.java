package servlet.head;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
import db.PeDbGoodsValue;
import service.UserService;
import servlet.CmServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

<<<<<<< HEAD
/**
 * @author xuwei
 */
@WebServlet(urlPatterns = "/buyHead")
public class BuyHeadServlet extends CmServletMain implements Serializable {
    @Override
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content) {
        String coinMagic = "coin";
=======
@WebServlet(urlPatterns = "/buyHead")
public class BuyHeadServlet extends CmServletMain implements Serializable
{
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        //用户信息
        String ddUid = content.getString("uid");
        String appId = content.getString("appId");
        //商品编号
        int goodsId = content.getInteger("goodsId");
        JSONObject result = new JSONObject();
        PeDbGoodsValue goodsValue = PeDbGoodsValue.getGoodsFast(goodsId);
<<<<<<< HEAD
        if (goodsValue == null) {
=======
        if (goodsValue == null)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            result.put("result", "fail!");
            result.put("msg", "商品不存在！");
            return result;
        }
        int avatarFrame = goodsValue.ddValue;
        //头像
<<<<<<< HEAD
        if (UserService.existAvatarFrame(ddUid, avatarFrame)) {
=======
        if (UserService.existAvatarFrame(ddUid, avatarFrame))
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            result.put("msg", "该头像框已存在，无需重复购买~");
            result.put("result", "fail");
            JSONObject user = new JSONObject();
            UserService.putUserValue(user, ddUid);
            result.put("user", user);
            return result;
        }
        int userValue = UserService.getValue(ddUid, "coin");
        int price = goodsValue.ddPrice.intValue();
        //商城购买
        JSONObject extra = new JSONObject();
        extra.put("type", "head");
        extra.put("appId", appId);
        extra.put("extra", String.valueOf(goodsId));
        long state;
<<<<<<< HEAD
        if (userValue < price || (state = UserService.addValue(ddUid, coinMagic, -price, extra)) < 0) {
=======
        if (userValue < price || (state = UserService.addValue(ddUid, "coin", -price, extra)) < 0)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            result.put("msg", "当前金币不足，不能购买！");
            result.put("result", "fail");
            JSONObject user = new JSONObject();
            UserService.putUserValue(user, ddUid);
            result.put("user", user);
            return result;
        }
<<<<<<< HEAD
        result.put(coinMagic, UserService.getValue(ddUid, coinMagic));
        result.put("result", state != userValue ? "success" : "fail");
        if (state != userValue) {
=======
        result.put("coin", UserService.getValue(ddUid, "coin"));
        result.put("result", state != userValue ? "success" : "fail");
        if (state != userValue)
        {
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            result.put("avatarFrameGain", UserService.addUserFrame(ddUid, avatarFrame));
        }
        return result;
    }
}
