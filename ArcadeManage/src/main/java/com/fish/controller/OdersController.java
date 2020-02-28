package com.fish.controller;

import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowOrders;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/manage")
public class OdersController
{
    @Autowired
    OrdersService ordersService;

    //查询订单信息
    @ResponseBody
    @GetMapping(value = "/orders")
    public GetResult getOders(GetParameter parameter)
    {
        return ordersService.findAll(parameter);
    }

    @ResponseBody
    @PostMapping(value = "/orders/single")
    public GetResult getSingleOders(@RequestBody ShowOrders showOrder)
    {
        GetResult result = new GetResult();
        Orders orders = showOrder.getOrders();
        String ddid = orders.getDdid();
        ShowOrders resOrder = ordersService.singleOrder(ddid);
        ArrayList<ShowOrders> objects = new ArrayList<>();
        if (resOrder != null)
        {
            objects.add(resOrder);
            result.setData(objects);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("订单查询失败，请联系管理员");
            return result;
        }

    }

    //新增订单信息
    @ResponseBody
    @PostMapping(value = "/orders/new")
    public PostResult insertOders(@RequestBody Orders productInfo)
    {
        PostResult result = new PostResult();

        int count = ordersService.insert(productInfo);
        // count =1;
        if (count == 1)
        {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","matchday");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

    //修改订单信息
    @ResponseBody
    @PostMapping(value = "/orders/edit")
    public PostResult modifyOders(@RequestBody Orders productInfo)
    {
        PostResult result = new PostResult();
        //  int count =1;
        int count = ordersService.updateByPrimaryKeySelective(productInfo);
        if (count != 0)
        {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","matchday");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else
        {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }

}
