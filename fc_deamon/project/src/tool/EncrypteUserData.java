package tool;

public class EncrypteUserData
{
	// openid
	public String openId;

	// 昵称
	public String nickName;

	// 性别
	public int sex;

	// 城市
	public String city;

	// 省市
	public String province;

	// 国家
	public String country;

	// 语言
	public String language;

	// 头像url
	public String avatarUrl;

	@Override
	public String toString()
	{
		return "EncrypteUserData [openId=" + openId + ", nickName=" + nickName
				+ ", sex=" + sex + ", language=" + language + ", city=" + city
				+ ", province=" + province + ", country=" + country
				+ ", avatarUrl=" + avatarUrl + "]";
	}
}
