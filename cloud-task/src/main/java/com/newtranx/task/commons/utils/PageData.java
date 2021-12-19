package com.newtranx.task.commons.utils;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 说明：参数封装Map 
 * 作者：作者：Ahui 1542933553qq
 *
 */
public class PageData extends HashMap<Object, Object> implements Map<Object, Object> {

	private static final long serialVersionUID = 1L;

	Map<Object, Object> map = null;
	HttpServletRequest request;

	public PageData(HttpServletRequest request) {
		this.request = request;
		Map properties = request.getParameterMap();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		Iterator<Object> entries = properties.entrySet().iterator();
		Entry<Object, Object> entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Entry<Object, Object>) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj || valueObj.toString().equals("undefined")) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1).equals("undefined")?"":value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		map = returnMap;
	}

	public PageData() {
		map = new HashMap<Object, Object>();
	}

	@Override
	public Object get(Object key) {
		Object obj = null;
		if (map.get(key) instanceof Object[]) {
			Object[] arr = (Object[]) map.get(key);
			obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		if (value instanceof ClobProxyImpl) { // 读取oracle Clob类型数据
			try {
				ClobProxyImpl cpi = (ClobProxyImpl) value;
				Reader is = cpi.getCharacterStream(); // 获取流
				BufferedReader br = new BufferedReader(is);
				String str = br.readLine();
				StringBuffer sb = new StringBuffer();
				while (str != null) { // 循环读取数据拼接到字符串
					sb.append(str);
					sb.append("\n");
					str = br.readLine();
				}
				value = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void clear() {
		map.clear();
	}
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}
	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}
	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

	@Override
	public void putAll(Map t) {
		// TODO Auto-generated method stub
		map.putAll(t);
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}
	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return map.values();
	}

}
