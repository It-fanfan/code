package com.code.dao.entity.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserIdDTO
{
    @Column(name = "userid")
    public long userId;
}
