package com.code.dao.entity.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
public class TimestampDTO
{
    @Column(name = "time")
    public Timestamp time;
}
