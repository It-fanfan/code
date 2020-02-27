package tool.geoIp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

public class GeoIpTools
{
    private static final String CONFIG_FILE = "GeoLite2-City.mmdb";

    /**
     * 全局静态变量，DatabaseReader，保证类加载时加载一次
     */
    private static DatabaseReader reader;

    static
    {
        try
        {
            InputStream in = GeoIpTools.class.getResourceAsStream("/" + CONFIG_FILE);
            reader = new DatabaseReader.Builder(in).build();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 解析IP
     */
    public static IPEntity getIPMsg(String ip)
    {
        IPEntity msg = new IPEntity();
        try
        {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            Country country = response.getCountry();
            Subdivision subdivision = response.getMostSpecificSubdivision();
            City city = response.getCity();
            Postal postal = response.getPostal();
            Location location = response.getLocation();

            msg.setCountryName(country.getNames().get("zh-CN"));
            msg.setCountryCode(country.getIsoCode());
            msg.setProvinceName(subdivision.getNames().get("zh-CN"));
            msg.setProvinceCode(subdivision.getIsoCode());
            msg.setCityName(city.getNames().get("zh-CN"));
            msg.setPostalCode(postal.getCode());
            //经度
            msg.setLongitude(location.getLongitude());
            //纬度
            msg.setLatitude(location.getLatitude());

        } catch (IOException | GeoIp2Exception e)
        {
            //e.printStackTrace();
        }
        return msg;
    }

    public static void main(String[] args)
    {
        IPEntity ipEntity = getIPMsg("49.246.255.255");
        System.out.println(ipEntity);
//                Set<String> codes = new HashSet<>();
//                for (int i = 0; i < 256; i++)
//                {
//                    for (int j = 0; j < 256; j++)
//                    {
//                        for (int k = 0; k < 256; k++)
//                        {
//                            for (int l = 0; l < 256; l++)
//                            {
//                                String ip = i + "." + j + "." + k + "." + l;
//                                try
//                                {
//                                    IPEntity ipEntity = getIPMsg(ip);
//                                    if (ipEntity != null && ipEntity.getCountryCode() != null && ipEntity.getCountryCode().equals("CN"))
//                                    {
//
//                                        if (!codes.contains(ipEntity.getProvinceCode()))
//                                            System.out.println(ip + "=" + getIPMsg(ip));
//                                        codes.add(ipEntity.getProvinceCode());
//                                    }
//                                } catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }
//                    }
//                }


    }

}