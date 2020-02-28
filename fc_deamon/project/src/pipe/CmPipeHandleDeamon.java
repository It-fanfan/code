package pipe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.PeDbGame;
import db.PeDbObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;

import java.util.Vector;

/**
 * @author feng
 */
<<<<<<< HEAD
class CmPipeHandleDeamon
=======
public class CmPipeHandleDeamon
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
{

    private static Logger LOG = LoggerFactory.getLogger(CmPipeHandleDeamon.class);

    /**
     * 处理管道命令的方法
     */
<<<<<<< HEAD
    static void handle(JSONObject object, CmPipeSocket socket)
=======
    public static void handle(JSONObject object, CmPipeSocket socket)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        try
        {
            String type = object.getString("type");
            switch (type)
            {
                //获取房间数据
                case "pkGame":
                {
                    String key = object.getString("addr");
                    socket.setSocketKey(key);
                    JSONArray array = object.getJSONArray("message");
                    socket.setPkRoom(array);
                }
                break;
<<<<<<< HEAD
                //更新房间数据
                case "pkRoom":
                {
                    JSONObject roomInfo = object.getJSONObject("message");
                    socket.updatePkRoom(roomInfo);
                }
                break;
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                case "pkInit":
                {
                    socket.setPkService(object);
                    //发送游戏数据
                    Vector<PeDbObject> games = PeDbGame.getGames();
                    JSONObject gameArray = new JSONObject();
                    games.forEach(element ->
                    {
                        PeDbGame game = (PeDbGame) element;
                        if (null == game || 0 == game.ddAvailable)
                        {
                            return;
                        }
                        JSONObject gameObject = game.getMessage();
                        gameArray.put(String.valueOf(game.ddCode), gameObject);
                    });
                    JSONObject send = new JSONObject();
                    send.put("games", gameArray);
                    send.put("type", "games");
                    socket.sendMessage(send);
                }
                break;
<<<<<<< HEAD
                default:
                    break;
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            }
            LOG.debug("获取数据:" + object.toJSONString());
        } catch (Exception e)
        {
            e.printStackTrace();
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

}
