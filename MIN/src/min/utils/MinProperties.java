package min.utils;
/**
 * 개요 : properties 속성을 정의합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinProperties
{
	private String key;
	private String value;
	
	public MinProperties() {}
	
	/**
	 * 생성자
	 * @param key
	 * @param value
	 */
	public MinProperties(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	/**
	 * key 값을 반환합니다.
	 * @return
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * value 값을 반환합니다.
	 * @return
	 */
	public String getStringValue()
	{
		return value;
	}
	
	/**
	 * value 값을 반환합니다.
	 * @return
	 */
	public Boolean getBooleanValue()
	{
		return Boolean.valueOf(value);
	}
	
	/**
	 * value 값을 반환합니다.
	 * @return
	 */
	public Integer getIntegerValue()
	{
		return Integer.valueOf(value);
	}
	
	/**
	 * value 값을 반환합니다.
	 * @return
	 */
	public Double getDoubleValue()
	{
		return Double.valueOf(value);
	}
	
	/**
	 * value 값을 반환합니다.
	 * @return
	 */
	public Float getFloatValue()
	{
		return Float.valueOf(value);
	}
	
	/**
	 * key, value 값을 설정합니다.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() 
	{
		return "Properties[key="+key+", value="+value+"]";
	}
}