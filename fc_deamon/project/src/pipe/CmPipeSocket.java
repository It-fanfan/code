package pipe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
import sun.misc.BASE64Decoder;
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
import tool.Log4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

<<<<<<< HEAD
/**
 * @author xuwei
 */
public class CmPipeSocket
{
    private static final Logger LOG = LoggerFactory.getLogger(CmPipeSocket.class);
    /**
     * 接受數據
     */
    private ReceiveMessageThread receiveMessageThread;
    /**
     * 發送數據
     */
    private SendMessageThread sendMessageThread;
    /**
     * 是否正在运行
     */
    private boolean isRunning = false;

    /**
     * 待发送的对象队列
     */
    private Vector<JSONObject> sendingQueue = new Vector<>();

    /**
     * 数据输入流
     */
    private DataInputStream dataInputStream = null;

    /**
     * 数据输出流
     */
=======
public class CmPipeSocket
{
    private static final Logger LOG = LoggerFactory.getLogger(CmPipeSocket.class);
    //接受數據
    private ReceiveMessageThread receiveMessageThread;
    //發送數據
    private SendMessageThread sendMessageThread;
    //
    // 是否正在运行
    //
    private boolean isRunning = false;

    //
    // 待发送的对象队列
    //
    private Vector<JSONObject> sendingQueue = new Vector<>();

    //
    // 数据输入流
    //
    private DataInputStream dataInputStream = null;

    //
    // 数据输出流
    //
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    private DataOutputStream dataOutputStream = null;

    private String socketKey = null;

    private JSONArray pkRoom;

    private JSONObject pkService;

    public String getSocketKey()
    {
        return socketKey;
    }

    public JSONArray getPkRoom()
    {
        return pkRoom;
    }

<<<<<<< HEAD
    JSONObject getPkService()
=======
    public JSONObject getPkService()
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        return pkService;
    }

<<<<<<< HEAD
    void setSocketKey(String socketKey)
=======
    public void setSocketKey(String socketKey)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        this.socketKey = socketKey;
    }

<<<<<<< HEAD
    void setPkRoom(JSONArray pkRoom)
=======
    public void setPkRoom(JSONArray pkRoom)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        this.pkRoom = pkRoom;
        //进行更新权重参数
        CmPipeServiceDemon.updateWeight();
    }

<<<<<<< HEAD
    /**
     * 更新房间数据
     *
     * @param roomInfo 房间信息
     */
    void updatePkRoom(JSONObject roomInfo)
    {
        CmPipeServiceDemon.updateRoomInfo(roomInfo, pkRoom);
    }

    void setPkService(JSONObject pkService)
=======
    public void setPkService(JSONObject pkService)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        this.pkService = pkService;
    }

<<<<<<< HEAD
    CmPipeSocket(Socket socket)
=======
    public CmPipeSocket(Socket socket)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        try
        {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            sendMessageThread = new SendMessageThread();
            receiveMessageThread = new ReceiveMessageThread();
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    public void start()
    {
        isRunning = true;
        sendMessageThread.start();
        receiveMessageThread.start();
    }

    /**
<<<<<<< HEAD
     * 发送消息的方法
     */
    void sendMessage(JSONObject object)
=======
     * 判断是否正在运行
     */
    public boolean isWorking()
    {
        return isRunning;
    }

    /**
     * 发送消息的方法
     */
    public void sendMessage(JSONObject object)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    {
        sendingQueue.addElement(object);
    }

    /**
     * 发送消息的方法
     */
    private void consumeMessage()
    {
        if (sendingQueue.size() <= 0)
        {
            try
            {
                Thread.sleep(48);
            } catch (InterruptedException e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }
            return;
        }

        try
        {
            JSONObject object = sendingQueue.firstElement();
            sendingQueue.removeElementAt(0);

            String message = object.toJSONString();
<<<<<<< HEAD
            LOG.debug("sendData" + message);
=======
            LOG.debug("sendData"+message);
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
            dataOutputStream.writeUTF(message);
        } catch (IOException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }


    /**
     * 读取消息的方法
     */
    private void receiveMessage()
    {
        try
        {
            while (isRunning)
            {
                LOG.debug("receiveMessage");
<<<<<<< HEAD
                String base64 = dataInputStream.readUTF();
                if (base64.isEmpty())
                {
                    continue;
                }
                String content = decryptionBase64(base64);
=======
                String content = dataInputStream.readUTF();
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
                JSONObject requestObject = JSONObject.parseObject(content);
                LOG.debug("CmPipeServiceDemon get data:" + content);
                if (null == requestObject)
                {
                    continue;
                }
                CmPipeHandleDeamon.handle(requestObject, this);
                Thread.sleep(1);
            }
<<<<<<< HEAD
        } catch (Exception e)
=======
        } catch (InterruptedException | IOException e)
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        isRunning = false;
        CmPipeServiceDemon.removeSocket(this);
    }

    /**
<<<<<<< HEAD
     * base64解密
     *
     * @param base64 decode
     * @return data
     * @throws IOException error
     */
    private String decryptionBase64(String base64) throws IOException
    {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64);
        return new String(buffer, "utf-8");
    }

    /**
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
     * 发送消息的类
     */
    private class SendMessageThread extends Thread
    {
        /**
         * 执行的方法
         */
<<<<<<< HEAD
        @Override
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        public void run()
        {
            while (isRunning)
            {
                consumeMessage();
            }
        }

    }

    /**
     * 接收消息的类
     */
    private class ReceiveMessageThread extends Thread
    {
        /**
         * 运行的方法
         */
<<<<<<< HEAD
        @Override
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        public void run()
        {
            receiveMessage();
        }
    }
}
