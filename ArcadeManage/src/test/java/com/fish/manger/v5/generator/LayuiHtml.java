package com.fish.manger.v5.generator;

import com.fish.manger.v5.mapper.TableDao;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LayuiHtml
{
    @Autowired
    TableDao tableDao;

    //模板HTML
    private String templateHtml;

    private static final String SAVE_DIR = "/src/main/resources/static/pages";


    public LayuiHtml()
    {
        try
        {
            File file = ResourceUtils.getFile("classpath:generator/defaultModule.code");
            System.out.println("获取文件信息:" + file.getName());
            InputStream in = new FileInputStream(file);
            templateHtml = XwhTool.readInputStream(in);
            in.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 构造表数据
     */
    private Set<String> generatorTable()
    {
        Set<String> set = new HashSet<>();
                set.add("supplement_order");
        //        set.add("statistics_pay");
        //        set.add("statistics_user_month");
        //        set.add("statistics_user_day");
        //        set.add("statistics_user_week");
        //        set.add("statistics_user_retention");
        return set;
    }

    /**
     * 进行创建模板
     */
    public void generator()
    {
        generatorTable().forEach(this::createTable);
    }

    /**
     * 创建表项
     *
     * @param tableName 表名
     */
    private void createTable(String tableName)
    {
        List<Map> tables = tableDao.listTableColumn(tableName);
        CreateHtml createHtml = new CreateHtml(templateHtml);
        tables.forEach(map ->
        {
            String column = (String) map.get("COLUMN_NAME");
            String dataType = (String) map.get("DATA_TYPE");
            String comment = (String) map.get("COLUMN_COMMENT");
            String pri = (String) map.get("COLUMN_KEY");
            createHtml.addOnceColumn(setStrChange(column), dataType, comment, pri);
        });
        String html = createHtml.getHtml();
        //保存文件，存储路径
        try
        {
            File dir = new File(System.getProperty("user.dir"), SAVE_DIR);
            System.out.println("保存文件:" + dir.getPath());
            File file = new File(dir, setStrChange(tableName) + ".html");
            FileOutputStream out = new FileOutputStream(file);
            out.write(html.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 进行设置小驼峰
     *
     * @param str 字符数据
     * @return 小驼峰
     */
    private String setStrChange(String str)
    {
        String[] data = str.split("_");
        StringBuilder builder = new StringBuilder(data[0].toLowerCase());
        if (data.length > 1)
        {
            for (int i = 1; i < data.length; i++)
            {
                String line = data[i].toLowerCase();
                String start = line.substring(0, 1).toUpperCase();
                builder.append(start).append(line.substring(1));
            }
        }
        return builder.toString();
    }

    private static class CreateHtml
    {
        private String templateHtml;

        private StringBuilder columnBuilder;
        private StringBuilder dateBuilder;
        private StringBuilder formBuilder;

        private CreateHtml(String template)
        {
            this.templateHtml = template;
            columnBuilder = new StringBuilder();
            dateBuilder = new StringBuilder();
            formBuilder = new StringBuilder();
            columnBuilder.append(" {type: 'checkbox'}");
        }

        /**
         * 生成html文件信息
         *
         * @return 已经规格好的html
         */
        private String getHtml()
        {
            templateHtml = templateHtml.replace("#columns_render", columnBuilder.toString());
            templateHtml = templateHtml.replace("#date_render", dateBuilder.toString());
            templateHtml = templateHtml.replace("#form_render", formBuilder.toString());
            return templateHtml;
        }


        /**
         * 添加一列数据
         *
         * @param column   列名
         * @param dataType 列数据类型
         * @param comment  描述
         * @param pri      主键标志
         */
        private void addOnceColumn(String column, String dataType, String comment, String pri)
        {
            if (columnBuilder.length() > 0)
            {
                columnBuilder.append(",");
            }
            columnBuilder.append(String.format("\n{field: '%s', width: 120, title: '%s', sort: true}", column, comment));
            String hide = "";
            String verify = "lay-verify=\"required\"";
            if (pri.toLowerCase().equals("pri"))
            {
                hide = "layui-hide";
            } else
            {
                switch (dataType)
                {
                    case "date":
                    {
                        dateBuilder.append(String.format("laydate.render({ \n" + "elem: '#%s',\n" + "format: 'yyyy/MM/dd'});\n", column));
                    }
                    break;
                    case "timestamp":
                    case "datetime":
                    {
                        // js render
                        dateBuilder.append(String.format("laydate.render({ \n" + "elem: '#%s',\n" + "format:'yyyy/MM/dd HH:mm'\n" + "});\n", column));
                    }
                    break;
                    default:
                        break;
                }
            }

            String bulider = String.format("<div class=\"layui-form-item %s\">\n" + "<label class=\"layui-form-label\">%s</label>\n" + "    	<div class=\"layui-input-inline\">\n" + "        	<input type=\"text\" id=\"%s\" name=\"%s\"  %s  autocomplete=\"off\" class=\"layui-input layui-input\">\n" + "    	</div>\n" + "	</div>\n",
                    hide, comment, column, column, verify);
            formBuilder.append("\n").append(bulider);
        }

    }

}
