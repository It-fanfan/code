package com.fish.utils;

import com.fish.utils.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel
{
    private static final Logger LOG = LoggerFactory.getLogger(ReadExcel.class);
    private Workbook workbook = null;

    /**
     * 读取excel
     *
     * @param readFile 文件
     */
    public void readFile(File readFile)
    {
        try
        {
            String filename = readFile.getName();
            InputStream is = new FileInputStream(readFile);
            if (filename.endsWith(".xls"))
            {
                this.workbook = new HSSFWorkbook(is);
            } else if (filename.endsWith(".xlsx"))
            {
                this.workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }
    }

    /**
     * 获取也数据
     *
     * @param sheetIndex 索引
     * @return 返回参数
     */
    public Sheet getSheet(int sheetIndex)
    {
        return this.workbook.getSheetAt(sheetIndex);
    }

    /**
     * 获取页数据
     *
     * @param name 页名称
     * @return 返回参数
     */
    public Sheet getSheet(String name)
    {
        return this.workbook.getSheet(name);
    }

    /**
     * 讀取行列
     *
     * @param sheetIndex 读取列
     * @return 返回参数
     */
    public List<List<String>> read(int sheetIndex)
    {
        Sheet sheet = getSheet(sheetIndex);
        return read(sheet);
    }

    /**
     * 讀取行列
     *
     * @param sheet 读取对象
     * @return 返回参数
     */
    public List<List<String>> read(Sheet sheet)
    {
        List<List<String>> dataList = new ArrayList<>();

        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rowCount; i++)
        {
            Row row = sheet.getRow(i);
            if (null == row)
            {
                continue;
            }
            List<String> rowList = new ArrayList<>();
            for (int c = 0; c < row.getLastCellNum(); c++)
            {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (cell != null)
                {
                    switch (cell.getCellType())
                    {
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell))
                            {
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                cellValue = fmt.format(cell.getDateCellValue());
                            } else
                            {
                                Double d = cell.getNumericCellValue();
                                cellValue = d.toString();
                                if (null != cellValue && !"".equals(cellValue.trim()))
                                {
                                    String[] item = cellValue.split("[.]");
                                    if (1 < item.length && "0".equals(item[1]))
                                    {
                                        cellValue = item[0];
                                    }
                                }
                            }
                            break;

                        case BOOLEAN:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;

                        case BLANK:

                            cellValue = cell.getStringCellValue();
                            break;

                        case ERROR:

                            cellValue = "";
                            break;

                        case FORMULA:
                            cellValue = cell.getCellFormula();
                            break;

                        default:
                            cellValue = cell.getStringCellValue();
                    }
                }
                rowList.add(cellValue);
            }
            dataList.add(rowList);
        }
        return dataList;
    }
}
