package com.code.dao.entity.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PreviewDTO
{
    @Column(name = "userId")
    public long userId;
}
