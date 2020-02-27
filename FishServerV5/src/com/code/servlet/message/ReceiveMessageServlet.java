package com.code.servlet.message;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.message.MessageProtocol;
import com.code.service.message.NoticeService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/message/receive", name = "com.code.protocols.operator.message.MessageProtocol.Receive")
public class ReceiveMessageServlet extends ServletMain<MessageProtocol.ReceiveRequestImpl, MessageProtocol.ReceiveResponseImpl>
{

    @Override
    protected MessageProtocol.ReceiveResponseImpl doLogic(MessageProtocol.ReceiveRequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        MessageProtocol.ReceiveResponseImpl res = new MessageProtocol.ReceiveResponseImpl();
        res.status = true;
        OperatorBase.updateResponseCode(OperatorBase.ERROR.SUCCESS, res);
        //领取邮件
        UserCache userCache = getUserCache(request);
        NoticeService service = new NoticeService(userCache);
        OperatorBase.ERROR error = service.receiveMessage(req.id);
        OperatorBase.updateResponseCode(error, res);
        res.setUserValue(userCache);
        return res;
    }
}
