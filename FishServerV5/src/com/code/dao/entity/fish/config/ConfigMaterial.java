package com.code.dao.entity.fish.config;

import com.annotation.PrimaryKey;
import com.annotation.ReadOnly;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "config_material")
public class ConfigMaterial
{
    @PrimaryKey
    @Column(name = "id")
    private int id;
    @Column(name = "icon")
    private String icon;
    @Column(name = "describe")
    private String describe;
    @ReadOnly
    @Column(name = "total")
    private int total;

    //    /**
    //     * 随机获得一份材料数据
    //     *
    //     * @return 材料信息
    //     */
    //    public static ConfigMaterial randomHit() {
    //        //获取材料组件
    //        Vector<ConfigMaterial> materials = FishInfoDao.instance().getCacheListByClass(ConfigMaterial.class);
    //        int index = ThreadLocalRandom.current().nextInt(materials.size());
    //        return materials.elementAt(index);
    //    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }
}
