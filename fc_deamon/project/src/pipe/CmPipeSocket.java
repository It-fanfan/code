package pipe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

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

    public JSONObject getPkService()
    {
        return pkService;
    }

    public void setSocketKey(String socketKey)
    {
        this.socketKey = socketKey;
    }

    public void setPkRoom(JSONArray pkRoom)
    {
        this.pkRoom = pkRoom;
        //进行更新权重参数
        CmPipeServiceDemon.updateWeight();
    }

    public void setPkService(JSONObject pkService)
    {
        this.pkService = pkService;
    }

    public CmPipeSocket(Socket socket)
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
            LOG.debug("sendData"+message);
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
                String content = dataInputStream.readUTF();
                JSONObject requestObject = JSONObject.parseObject(content);
                LOG.debug("CmPipeServiceDemon get data:" + content);
                if (null == requestObject)
                {
                    continue;
                }
                CmPipeHandleDeamon.handle(requestObject, this);
                Thread.sleep(1);
            }
        } catch (InterruptedException | IOException e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        isRunning = false;
        CmPipeServiceDemon.removeSocket(this);
    }

    /**
     * 发送消息的类
     */
    private class SendMessageThread extends Thread
    {
        /**
         * 执行的方法
         */
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
        public void run()
        {
            receiveMessage();
        }
    }
}
