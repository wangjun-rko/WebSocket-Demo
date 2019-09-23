package com.wj88.websocket.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class SpringContextProvider implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext;
	}

	/**
	 * Get a Spring bean by type.
	 **/
	public static <T> T getBean(Class<T> beanClass) {
		return CONTEXT.getBean(beanClass);
	}
	
	/**
	 * Get Spring beans by type.
	 * @param <T>
	 **/
	public static <T> Map<String, T>  getBeans(Class<T> beanClass) {
		return CONTEXT.getBeansOfType(beanClass);
	}

	/**
	 * Get a Spring bean by name.
	 **/
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

	/**
	 * 根据指定类型获取对应的bean
	 * 
	 * @author wuxiaozeng
	 * @date 2018年3月9日
	 * @param type
	 *            可解析类型
	 * @return
	 */
	public static Object getBeanForType(ResolvableType type) {
		String[] beanNames = CONTEXT.getBeanNamesForType(type);
		if (beanNames == null || beanNames.length == 0){
			return null;
		}
		else if (beanNames.length > 1){
			throw new NoUniqueBeanDefinitionException(type.getRawClass(), "定义数量大于1个,无法获取Bean");
		}

		return getBean(beanNames[0]);
	}

}