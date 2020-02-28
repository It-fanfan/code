package com.fish.manger.v5;

import com.alibaba.fastjson.JSONObject;
import com.fish.utils.HttpPostUploadUtil;
import com.fish.utils.http.XwhHttp;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Tset {
 /*   public static void main(String[] args) throws IOException {
//         String appId = "wxa3767fede98509c6";
//         String secret = "8919594e4054a7a195643755daaafb41";
//         String accessToken = getAccessToken(appId,secret);
//         System.out.println(accessToken);

//        HttpPostUploadUtil util=new HttpPostUploadUtil();
//        String filepath="E:\\IdeaProjects\\CustomerService\\CustomerService\\src\\main\\resources\\微信扫码.jpg";
//        Map<String, String> textMap = new HashMap<String, String>();
//        textMap.put("", "微信扫码");
//        Map<String, String> fileMap = new HashMap<String, String>();
//        fileMap.put("userfile", filepath);
//        String mediaidrs = util.formUpload(textMap, fileMap);
//        System.out.println(mediaidrs);


//            String s = DigestUtils.md5Hex(new FileInputStream("C:/Users/Host-0/Desktop/备份/2019-11-15 19_06_03509"));
//           System.out.println(s+"__MD5");

      //  URL url = new URL("http://mmbiz.qpic.cn/sz_mmbiz_jpg/HPic4ZTicUAoicgIBX5S7eQtBh9Ny2AAMafqcB6lTyHX27j7LIPyxRwdzAMMxT2NdgmmhlVKTjF5YPtjdpf17icUAA/0");
        //URL url = new URL("http://mmbiz.qpic.cn/sz_mmbiz_jpg/HPic4ZTicUAoibhgeafg1YXSdx7IDDIM7zT3n5LwNIJGCsE4CJfPriah8JXefcxhPhCbuUncLY6fmhNicCT8wenVibCg/0");
//        URL url = new URL("http://mmbiz.qpic.cn/sz_mmbiz_jpg/HPic4ZTicUAoib0w4oav9Gc3jUKVriab7TshpCAHe14sI59fDwe5UvtmEUpyy0U9QPwHaFEOEJUpbdicLamrpfaicia3A/0");
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        InputStream in = conn.getInputStream();
//        String md5Hex = DigestUtils.md5Hex(in);
//        System.out.println(md5Hex);

    }
    *//**
     * 上传其他永久素材(图片素材的上限为5000，其他类型为1000)
     *
     * @return
     * @throws Exception
     *//*
    public static JSONObject addMaterialEver(String fileurl, String type, String token) {
        try {
            File file = new File(fileurl);
            //上传素材
            String path = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + token + "&type=" + type;
            String result = null;
            try {
                result = connectHttpsByPost(path,file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = result.replaceAll("[\\\\]", "");
            System.out.println("result:" + result);
            JSONObject resultJSON = JSONObject.parseObject(result);
            if (resultJSON != null) {
                if (resultJSON.get("media_id") != null) {
                    System.out.println("上传" + resultJSON.get("media_id") + "永久素材成功");
                    return resultJSON;
                } else {
                    System.out.println("上传" + type + "永久素材失败");
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  String connectHttpsByPost(String path, File file) throws Exception {
        URL urlObj = new URL(path);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        String result = null;
        con.setRequestMethod("POST");
        con.setDoInput(true);

        con.setDoOutput(true);

        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary="
                        + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""

                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);

        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

    *//**
     * 获取access_token
     *
     * @param appId 消息的参数
     * *//*
    public static String getAccessToken(String appId, String appSecret) {
        //获取access_token
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String resToken = null;
        try {
            resToken = XwhHttp.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject resDataObj = JSONObject.parseObject(resToken);
        String accessToken = (String) resDataObj.get("access_token");
        return accessToken;
    }*/
}
