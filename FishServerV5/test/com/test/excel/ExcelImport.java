package com.test.excel;

import com.code.dao.db.FishInfoDb;
import com.utils.XwhTool;
import com.utils.db.XWHResultSetMapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * excel导入
 */
public class ExcelImport<T> {

    private List<List<String>> data;
    private int index;
    private int tableIndex;
    private Class<T> outClass;

    ExcelImport(Class<T> outClass, List<List<String>> data, int index, int tableIndex) {
        this.data = data;
        this.index = index;
        this.tableIndex = tableIndex;
        this.outClass = outClass;
    }

    /**
     * 映射数据参数
     *
     * @param action 操作类型
     */
    public void mappingData(Consumer<? super T> action) {
        try {
            Vector<T> list = new Vector<>();
            Field[] fields = outClass.getDeclaredFields();
            List<String> first = data.get(0);
            System.out.println(XwhTool.getJSONByFastJSON(first));
            for (int i = 2; i < data.size(); i++) {
                List<String> value = data.get(i);
                T t = outClass.newInstance();
                for (int j = 0; j < first.size() - tableIndex; j++) {
                    Field field = fields[j + index];
                    String name = field.getName();
                    System.out.println(name + "<=>" + first.get(j + tableIndex));
                    field.setAccessible(true);
                    String str = value.get(j + tableIndex);
                    Class<?> type = field.getType();
                    if (type == int.class) {
                        if (str.trim().isEmpty()) {
                            field.set(t, 0);
                        } else if (str.equals("-")) {
                            field.set(t, -1);
                        } else if (str.startsWith("✔ - Lv.")) {
                            field.set(t, Integer.valueOf(str.replace("✔ - Lv.", "")));
                        } else if (str.startsWith("第")) {
                            str = str.replace("第", "").replace("位", "").trim();
                            field.set(t, Integer.valueOf(str));
                        } else {
                            field.set(t, Integer.valueOf(str));
                        }

                    } else if (type == long.class) {
                        if (str.trim().isEmpty()) {
                            field.set(t, 0);
                        } else
                            field.set(t, Long.valueOf(str));
                    } else if (type == double.class || type == float.class) {
                        if (str.trim().isEmpty()) {
                            field.set(t, 0);
                        } else
                            field.set(t, Double.valueOf(str));
                    } else {
                        field.set(t, str);
                    }
                }
                list.add(t);
            }
            System.out.println("对象:" + XwhTool.getJSONByFastJSON(list));
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(15);
            XWHResultSetMapper.init(scheduler);
            if (action != null) {
                for (T t : list) {
                    action.accept(t);
                }
            }
            FishInfoDb.instance().saveOrUpdate(list, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
