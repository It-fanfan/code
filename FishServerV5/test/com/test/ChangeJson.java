package com.test;

import com.code.dao.db.FishInfoDb;
import com.code.dao.entity.fish.config.ConfigBasin;
import com.code.dao.entity.fish.config.ConfigFish;
import com.code.dao.use.FishInfoDao;
import com.utils.XwhTool;
import com.utils.db.XWHResultSetMapper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class ChangeJson {
    public static void main(String[] args) {
        //        Vector<String[]> keys = new Vector<>();
        //        keys.add(new String[]{"1", "1"});
        //        keys.add(new String[]{"2", "2"});
        //        keys.add(new String[]{"3", "3"});
        //        String[][] num = new String[4][];
        //        num = keys.toArray(num);
        //        System.out.println(num);
        //        for (int i = 0; i < num.length; i++)
        //        {
        //            for (int p = 0; p < num[i].length; p++)
        //            {
        //                System.out.println(num[i][p]);
        //            }
        //        }
        /**
         * deck:Images/skin/1/$.lv/deck.png
         * helm:Images/skin/$.goodsId/$.lv/helm.png
         * ship:Images/skin/$.goodsId/default/ship.png
         * tabbtn:Images/skin/$.goodsId/default/tabbtn.png
         */
        //        Map<String, String> hash = new LinkedHashMap<>();
        //        hash.put("deck", "Images/skin/1/$.lv/deck.png");
        //        hash.put("helm", "Images/skin/1/$.lv/helm.png");
        //        hash.put("ship", "Images/skin/1/default/ship.png");
        //        hash.put("tabbtn", "Images/skin/1/default/tabbtn.png");
        //        String json = XwhTool.getJSONByFastJSON(hash);
        //        System.out.println(json);
        //        hash.clear();
        //        hash.put("homebg","Images/skin/1/$.lv/homebg.png");
        //        hash.put("pacificbg","Images/skin/1/$.lv/pacificbg.png");
        //        json = XwhTool.getJSONByFastJSON(hash);
        //        System.out.println(json);

        Map<String, Object> map = new LinkedHashMap<>();
        Vector<String> title = new Vector<>();
        title.add("a");
        title.add("b");
        title.add("c");
        title.add("d");
        Vector<Vector<Object>> data = new Vector<>();
        for (int i = 0; i < 6; i++) {
            Vector<Object> objects = new Vector<>();
            for (int j = 0; j < title.size(); j++) {
                Object value;
                switch (ThreadLocalRandom.current().nextInt(6)) {
                    case 0:
                        value = ThreadLocalRandom.current().nextBoolean();
                        break;
                    case 1:
                        value = ThreadLocalRandom.current().nextDouble(100);
                        break;
                    case 2:
                        value = ThreadLocalRandom.current().nextFloat();
                        break;
                    case 3:
                        value = ThreadLocalRandom.current().nextInt(100);
                        break;
                    case 4:
                        value = ThreadLocalRandom.current().nextLong(10000000L);
                        break;
                    default:
                        value = UUID.randomUUID().toString();
                        break;

                }
                objects.add(value);
            }
            data.add(objects);
        }
        map.put("title", title);
        map.put("data", data);
        System.out.println(XwhTool.getJSONByFastJSON(map));
    }

    public static class ConfigBasint {
        public int basinid;
        public String basinrank;
        public String rankname;
        public int[] archivename;
        public String chinesename;
        public String englingname;
        public int depth;
        public int addamount;
        public int addshellamount;
    }

    public static void configBasin() {
        // 线程池
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(15);
        XWHResultSetMapper.init(scheduler);
        Vector<ConfigBasin> bySQL = FishInfoDao.instance().findBySQL("select * from config_basin", ConfigBasin.class);
        bySQL.sort(Comparator.comparingInt(ConfigBasin::getBasinId));
        Vector<ConfigBasint> data = new Vector<>();
        for (ConfigBasin en : bySQL) {
            ConfigBasint test = new ConfigBasint();
            test.basinid = en.getBasinId();
            test.chinesename = en.getChineseName();
            int[] arname = new int[en.getArchiveName()];
            Vector<ConfigFish> bySQL1 = FishInfoDb.instance().findBySQL("select * from config_fish", ConfigFish.class);
            int i = 0;
            for (ConfigFish ens : bySQL1) {
                if (ens.getBasin() == en.getBasinId()) {
                    arname[i++] = ens.getFtId();
                }
            }

            test.archivename = arname;
            data.add(test);
        }
        String jsonByFastJSON = XwhTool.getJSONByFastJSON(data);
        System.out.println(jsonByFastJSON);
        System.out.println(XwhTool.getJSONByFastJSON(bySQL));
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\configbasin.json"));
            if (jsonByFastJSON != null) {
                out.write(jsonByFastJSON.getBytes("utf-8"));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
