package net.dubboclub.dubbogenerator.reference;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bieber on 2015/8/7.
 */
public class InitializingFacade implements InitializingBean,BeanFactoryAware{

    private List<Class<?>> clientClassList;
    
    private DefaultListableBeanFactory  beanFactory;
    
    private AtomicLong facadeBeanCount = new AtomicLong(0);
    
    @Override
    public void afterPropertiesSet() throws Exception {
        if(clientClassList!=null&&clientClassList.size()>0){
            for(Class<?> clientType:clientClassList){
                DubboClientWrapper.getWrapper(clientType);
                registerSpringBean(clientType);
            }
        }
    }
    
    private void registerSpringBean(Class<?> type){
        RootBeanDefinition definition = new RootBeanDefinition();
        definition.setBeanClass(ClientFactory.class);
        definition.setScope(BeanDefinition.SCOPE_SINGLETON);
        definition.setLazyInit(false);
        definition.getPropertyValues().addPropertyValue("type",type);
        beanFactory.registerBeanDefinition(type.getName(),definition);
    }

    public void setClientClassList(List<Class<?>> clientClassList) {
        this.clientClassList = clientClassList;
    }



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
