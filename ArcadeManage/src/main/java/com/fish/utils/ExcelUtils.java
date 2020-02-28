package com.fish.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils
{

    private static final String dateFormatPattern = "yyyy-MM-dd";

    private static final String dateTimeFormatPattern = "yyyy-MM-dd HH:mm:ss";


    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);


    /**
     * 将查询到的数据封装入Excel工作簿接口
     *
     * @param list           需要导出的数据集合
     * @param fileNamePrefix 文件名不含后缀
     * @return Excel实体
     */
    public static Workbook writeExcel(List<?> list, String fileNamePrefix)
    {
        if (CollectionUtils.isEmpty(list) || StringUtils.isBlank(fileNamePrefix))
            return null;
        Class clazz = null;
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext())
        {
            Object obj = iterator.next();
            if (obj != null)
            {
                clazz = obj.getClass();
                break;
            } else
                iterator.remove();
        }
        if (clazz == null)
            return null;

        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        fields.removeIf(next -> Modifier.isStatic(next.getModifiers()) || next.isAnnotationPresent(NonExcel.class));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileNamePrefix);
        for (int i = 0; i < list.size(); i++)
        {
            Object obj = list.get(i);
            try
            {
                if (i == 0)
                {
                    Row row0 = sheet.createRow(0);
                    Row row1 = sheet.createRow(1);
                    for (int j = 0; j < fields.size(); j++)
                    {
                        sheet.setColumnWidth(j, 6000);

                        Field field = fields.get(j);
                        field.setAccessible(true);

                        String commentsName = getCommentsName(field);
                        row0.createCell(j).setCellValue(commentsName);
                        row1.createCell(j).setCellValue(field.get(obj) == null ? "" : field.get(obj).toString());
                    }
                } else
                {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < fields.size(); j++)
                    {
                        Field field = fields.get(j);
                        field.setAccessible(true);

                        row.createCell(j).setCellValue(field.get(obj) == null ? "" : field.get(obj).toString());
                    }
                }
            } catch (IllegalAccessException e)
            {
                logger.error("ExcelUtils 生成Workbook失败", e);
            }
        }
        return workbook;
    }

    /**
     * 直接导出集合数据
     *
     * @param list           导出数据集合
     * @param fileNamePrefix Excel文件名前缀（不含.xls）
     * @param response       返回对象
     */
    public static void writeExcel(List<?> list, String fileNamePrefix, HttpServletResponse response)
    {
        if (response == null) return;
        Workbook workbook = writeExcel(list, fileNamePrefix);
        try
        {
            response.setHeader("content-disposition",
                    "attachment;" + "filename=" + URLEncoder.encode(fileNamePrefix + ".xlsx", "UTF-8"));

            if (workbook != null)
            {
                workbook.write(response.getOutputStream());
            }
        } catch (IOException e)
        {
            logger.error("ExcelUtils 导出集合数据失败", e);
        }
    }


    private static String getCommentsName(Field field)
    {
        return field.isAnnotationPresent(Comments.class) ? (String) AnnotationUtils.getValue(field.getAnnotation(Comments.class), "name") : "";
    }

}
