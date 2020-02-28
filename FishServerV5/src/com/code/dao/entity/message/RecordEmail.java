package com.code.dao.entity.message;

import javax.persistence.Entity;

@Entity(name = "record_email")
public class RecordEmail extends Email
{

    public RecordEmail(Email element)
    {
        setButton(element.getButton());
        setContext(element.getContext());
        setIcon(element.getIcon());
        setLeaveType(element.getLeaveType());
        setReward(element.getReward());
        setId(element.getId());
        setUserId(element.getUserId());
        setMessageType(element.getMessageType());
        setTimes(element.getTimes());
        setTitle(element.getTitle());
        setParameters(element.getParameters());
    }
}
