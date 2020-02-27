package com.code.servlet.operate.ext;

import com.code.protocols.AbstractResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Vector;

public class Refresh
{
    public static class Request
    {
        public boolean isall;
        public Vector<Long> userids;
    }

    public static class Response extends AbstractResponse
    {
    }

    @Entity
    public static class TableName
    {
        @Column(name = "tn")
        public String table;
    }
}
