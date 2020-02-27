package com.fish.utils;
import java.io.*;

public class ReadTxtFileTool {
    /**
     * 读取json文件，返回json串
     * @param Path
     * @return
     */
    public String ReadFile(String Path){
            BufferedReader reader = null;
            String laststr = "";
            try{
                FileInputStream fileInputStream = new FileInputStream(Path);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                reader = new BufferedReader(inputStreamReader);
                String tempString = null;
                while((tempString = reader.readLine()) != null){
                    laststr += tempString;
                }
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return laststr;
        }

    }
