package com.code.servlet.message;

import com.annotation.AvoidRepeatableCommit;
import com.code.cache.UserCache;
import com.code.protocols.basic.BigData;
import com.code.protocols.operator.OperatorBase;
import com.code.protocols.operator.message.MessageProtocol.SelectRequestImpl;
import com.code.protocols.operator.message.MessageProtocol.SelectResponseImpl;
import com.code.service.message.NoticeService;
import com.code.servlet.base.ServletMain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Vector;

@AvoidRepeatableCommit
@WebServlet(urlPatterns = "/message/select", name = "com.code.protocols.operator.message.MessageProtocol.Select")
public class SelectMessageServlet extends ServletMain<SelectRequestImpl, SelectResponseImpl>
{
    @Override
    protected SelectResponseImpl doLogic(SelectRequestImpl req, HttpServletRequest request, HttpServletResponse response)
    {
        SelectResponseImpl res = new SelectResponseImpl();
        res.code = STATUS_SUCCESS;
        res.status = true;
        UserCache userCache = getUserCache(request);
        NoticeService noticeService = new NoticeService(userCache);
        Vector<OperatorBase.MessageInfo> system = noticeService.getSystemEmail(req.histroyunclaimed);
        res.messages = BigData.getBigData(system, OperatorBase.MessageInfo.class);
        noticeService.updateMessageFlag(-system.size());
        return res;
    }
}
