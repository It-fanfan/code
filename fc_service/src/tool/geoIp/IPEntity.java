package tool.geoIp;

public class IPEntity
{
    //国家
    private String countryName;
    //国家代码
    private String countryCode;

    //省份
    private String provinceName;
    private String provinceCode;

    //城市名称
    private String cityName;

    //邮政编码
    private String postalCode;

    //经度
    private Double longitude;
    //纬度
    private Double latitude;

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }

    public String getProvinceCode()
    {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode)
    {
        this.provinceCode = provinceCode;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return "IPEntity{" + "countryName='" + countryName + '\'' + ", countryCode='" + countryCode + '\'' + ", provinceName='" + provinceName + '\'' + ", provinceCode='" + provinceCode + '\'' + ", cityName='" + cityName + '\'' + ", postalCode='" + postalCode + '\'' + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }
}
