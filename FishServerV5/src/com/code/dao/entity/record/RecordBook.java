package com.code.dao.entity.record;

import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "record_book")
public class RecordBook
{
    @PrimaryKey
    @Column(name = "id")
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "book")
    private int book;
    @Column(name = "bookName")
    private String bookName;
    @Column(name = "basinId")
    private int basinId;
    @Column(name = "basinName")
    private String basinName;
    @Column(name = "lighten")
    private int lighten;
    @Column(name = "updateTime")
    private java.sql.Timestamp updateTime;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }


    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }


    public int getBook()
    {
        return book;
    }

    public void setBook(int book)
    {
        this.book = book;
    }


    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }


    public int getBasinId()
    {
        return basinId;
    }

    public void setBasinId(int basinId)
    {
        this.basinId = basinId;
    }


    public String getBasinName()
    {
        return basinName;
    }

    public void setBasinName(String basinName)
    {
        this.basinName = basinName;
    }


    public int getLighten()
    {
        return lighten;
    }

    public void setLighten(int lighten)
    {
        this.lighten = lighten;
    }


    public java.sql.Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(java.sql.Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

}
