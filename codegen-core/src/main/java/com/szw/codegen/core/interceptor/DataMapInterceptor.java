package com.szw.codegen.core.interceptor;

import com.szw.codegen.core.Engine;
import com.szw.codegen.core.Interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据映射拦截器。可在这个拦截器中完成修改原始数据在模板中对应的名字、对原始数据字段进行过滤等操作。
 * <p>
 * 这个拦截器会将 data 类型由 TableMetaData 修改为 Map，故在模板中使用 data 的格式可能会改变。参考{@link Engine#merge(String, Object)}和{@link Engine#merge(String, Map)}注解上的说明。
 *
 * @author SZW
 */
public class DataMapInterceptor<D, T, R> implements Interceptor<T, R> {
	private final Mapper<D> mapper;


	private final Class<D> dataClass;

	/**
	 * 由于lambda表达式的泛型擦除，需要手动指定 data 类型
	 */
	public DataMapInterceptor(Mapper<D> mapper, Class<D> dataClass) {
		this.mapper = mapper;
		this.dataClass = dataClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public R intercept(T template, Object data, Chain<T, R> chain) {

		if (dataClass.isAssignableFrom (data.getClass ())) {

			HashMap<String, Object> dataMap = new HashMap<> ();
			mapper.doMapping ((D) data, dataMap);

			return chain.proceed (template, dataMap);
		}

		return chain.proceed (template, data);
	}

	public interface Mapper<D> {
		void doMapping(D rawData, Map<String, Object> dataMap);
	}
}
