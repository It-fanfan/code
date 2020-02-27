package com.code.dao.entity.fish.config;

import com.annotation.KeyAuto;
import com.annotation.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Field;

@Entity(name = "config_basin")
public class ConfigBasin
{
    @KeyAuto
    @PrimaryKey
    @Column(name = "basinId")
    private int basinId;
    @Column(name = "archiveName")
    private int archiveName;
    @Column(name = "chineseName")
    private String chineseName;
    @Column(name = "openCost")
    private int openCost;
    @Column(name = "materials")
    private String materials;

    public static Field[] getInitField() throws NoSuchFieldException
    {
        return new Field[]{ConfigBasin.class.getDeclaredField("basinId"),
                ConfigBasin.class.getDeclaredField("openCost"),
                ConfigBasin.class.getDeclaredField("materials")};
    }


    public int getBasinId()
    {
        return basinId;
    }

    public void setBasinId(int basinId)
    {
        this.basinId = basinId;
    }

    public int getArchiveName()
    {
        return archiveName;
    }

    public void setArchiveName(int archiveName)
    {
        this.archiveName = archiveName;
    }

    public String getChineseName()
    {
        return chineseName;
    }

    public void setChineseName(String chineseName)
    {
        this.chineseName = chineseName;
    }

    public int getOpenCost()
    {
        return openCost;
    }

    public void setOpenCost(int openCost)
    {
        this.openCost = openCost;
    }

    public String getMaterials()
    {
        return materials;
    }

    public void setMaterials(String materials)
    {
        this.materials = materials;
    }
}
