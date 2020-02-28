package com.test.excel;

import com.utils.ReadExcel;
import com.utils.XwhTool;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ReadExcel excel = new ReadExcel();
        File readFile = new File("E:\\products\\P18001_FishGame\\public\\Document\\1.项目设计\\3.数值策划\\V5\\V5 数值.xlsx");
        excel.readFile(readFile);
        List<List<String>> data = excel.read(excel.getSheet("英译"));
        Map<String,String> en = new LinkedHashMap<>();
        data.forEach(list->en.put(list.get(0),list.get(1)));
        System.out.println(XwhTool.getJSONByFastJSON(en));
//        List<List<String>> data = excel.read(excel.getSheet("普通图鉴"));
//        System.out.println(data.size() + ";json=" + XwhTool.getJSONByFastJSON(data));
//        ExcelImport excelImport = new ExcelImport<>(ConfigFish.class, data, 0, 1);
//        excelImport.mappingData(element -> {
//            ConfigFish _v = (ConfigFish) element;
//            String typeName = getPingYin(_v.getTypeName());
//            _v.setIcon(typeName);
//            _v.setSkelName(typeName);
//        });
        System.exit(1);
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputString 汉字
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String output = "";
        if (inputString != null && inputString.length() > 0
                && !"null".equals(inputString)) {
            char[] input = inputString.trim().toCharArray();
            try {
                for (int i = 0; i < input.length; i++) {
                    if (java.lang.Character.toString(input[i]).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                                input[i], format);
                        output += temp[0];
                    } else
                        output += java.lang.Character.toString(input[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            return "*";
        }
        return output;
    }
}
