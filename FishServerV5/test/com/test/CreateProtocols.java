package com.test;

import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.entity.fish.config.ConfigRate;
import com.code.dao.entity.fish.config.ConfigShare;
import com.code.protocols.basic.BigData;
import com.utils.XWHMathTool;
import com.utils.XwhTool;
import com.utils.db.XWHResultSetMapper;
import com.utils.font.Localization;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CreateProtocols {
    // 线程池
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(15);
    private String localization;

    public CreateProtocols(String localization) {
        this.localization = localization;
        XWHResultSetMapper.init(scheduler);
    }

    /**
     * 创建fishtype对象
     */
    public void createFishTypes() {
        Vector<ConfigFish> data = FishInfoDb.instance().getCacheListByClass(ConfigFish.class);
        try {
            data.sort(Comparator.comparingInt(ConfigFish::getFtId));
            for (ConfigFish fish : data) {
                Localization.getInstance(localization).convert(fish);
                System.out.println(fish.getTypeName());
                System.out.println(fish.getDescription());
            }
            String json = XwhTool.getJSONByFastJSON(BigData.getBigData(data, ConfigFish.class));
            XwhTool.writeFile("C:\\Workspaces\\fishtypes_" + localization + ".json", json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建喂食节点信息
     */
    public void createFeedDetail() {
        String path = "C:\\Workspaces\\feeddetail.json";
        try {
            Vector<ConfigRate> data = FishInfoDb.instance().getCacheListByClass(ConfigRate.class);
            Map<String, Object> map = new LinkedHashMap<>();
            data.sort(Comparator.comparingInt(ConfigRate::getFtId));
            Field[] fields = ConfigRate.class.getDeclaredFields();
            for (ConfigRate rate : data) {
                Map<String, Object> element = new LinkedHashMap<>();
                fields[1].setAccessible(true);
                element.put(fields[1].getName().toLowerCase(), fields[1].get(rate));
                Vector<Object> factor = new Vector<>();
                for (int i = 2; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Double value = fields[i].getDouble(rate);
                    if (value == 0) {
                        break;
                    }
                    factor.add(XWHMathTool.formatMath(value, 3));
                }
                element.put("factor", factor);
                map.put(String.valueOf(rate.getFtId()), element);
            }
            String json = XwhTool.getJSONByFastJSON(map);
            System.out.println(json);
            XwhTool.writeFile(path, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建水域对象
     */
    public void createConfigBasin() {
        Vector<ConfigBasin> data = FishInfoDb.instance().getCacheListByClass(ConfigBasin.class);
        Vector<Map<String, Object>> vector = new Vector<>();
        try {
            Field[] fields = ConfigBasin.class.getDeclaredFields();
            data.sort(Comparator.comparingInt(ConfigBasin::getBasinId));
            for (ConfigBasin element : data) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (Field field : fields) {
                    String key = field.getName().toLowerCase();
                    if ("archivename".equals(key)) {
                        map.put(key, getFtIdByBasinId(element.getBasinId()));
                        continue;
                    }
                    field.setAccessible(true);
                    map.put(key, field.get(element));
                }
                vector.add(map);
            }
            String json = XwhTool.getJSONByFastJSON(vector);
            System.out.println(json);
            XwhTool.writeFile("C:\\Workspaces\\configbasin.json", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Vector<Integer> getFtIdByBasinId(int basinId) {
        Vector<Integer> ftIds = new Vector<>();
        Vector<ConfigFish> data = FishInfoDb.instance().getCacheListByClass(ConfigFish.class);
        data.sort(Comparator.comparingInt(ConfigFish::getFtId));
        for (int i = 0; i < data.size(); i++) {
            ConfigFish configFish = data.get(i);
            if (configFish.getBasin() == basinId) {
                ftIds.add(configFish.getFtId());
            }
        }
        return ftIds;
    }

    public BigData getConfigShare() {
        return BigData.getBigData(FishInfoDb.instance().getCacheListByClass(ConfigShare.class), ConfigShare.class);
    }

    public static void main(String[] args) {
        CreateProtocols protocols = new CreateProtocols("ch");
        protocols.createConfigBasin();
        //        System.out.println(XwhTool.getJSONByFastJSON(protocols.getc()));
//        protocols.createFeedDetail();
        //                protocols.createFeedDetail();
        //        protocols.createConfigBasin();
        System.exit(1);
    }
}
