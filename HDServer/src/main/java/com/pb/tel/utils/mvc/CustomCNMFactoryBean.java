package com.pb.tel.utils.mvc;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;

/**
 * Created by vladimir on 06.08.19.
 */
public class CustomCNMFactoryBean implements FactoryBean<ContentNegotiationManager>, InitializingBean {
    private ContentNegotiationManager contentNegotiationManager;
    private ContentNegotiationStrategy contentNegotiationStrategy;

    private CustomCNMFactoryBean(ContentNegotiationStrategy contentNegotiationStrategy) {
        this.contentNegotiationStrategy = contentNegotiationStrategy;
    }

    public void afterPropertiesSet() {
        this.contentNegotiationManager = new ContentNegotiationManager(new ContentNegotiationStrategy[]{this.contentNegotiationStrategy});
    }

    public ContentNegotiationManager getObject() throws Exception {
        return this.contentNegotiationManager;
    }

    public Class<?> getObjectType() {
        return ContentNegotiationManager.class;
    }

    public boolean isSingleton() {
        return true;
    }
}