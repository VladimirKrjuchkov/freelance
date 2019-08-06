package com.pb.tel.config;

import com.pb.tel.utils.logging.HttpServletRequestResponseLoggingFilter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses(){
		return new Class[]{RootConfig.class, ServiceConfig.class, SecurityConfig.class, WebSocketConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses(){
		return new Class[]{ServletConfig.class};
	}


	@Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


	@Override
	protected void registerDispatcherServlet(ServletContext servletContext) {
		String servletName = getServletName();
		Assert.hasLength(servletName, "getServletName() must not return null or empty");

		WebApplicationContext servletAppContext = createServletApplicationContext();
		Assert.notNull(servletAppContext, "createServletApplicationContext() must not return null");

		FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
		Assert.notNull(dispatcherServlet, "createDispatcherServlet(WebApplicationContext) must not return null");
		dispatcherServlet.setContextInitializers(getServletApplicationContextInitializers());

		ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
		if (registration == null) {
			throw new IllegalStateException("Failed to register servlet with name '" + servletName + "'. " +
					"Check if there is another servlet registered under the same name.");
		}

		registration.setLoadOnStartup(1);
		registration.addMapping(getServletMappings());
		registration.setAsyncSupported(isAsyncSupported());

		servletContext.setSessionTrackingModes(getSessionTrackingModes());

		registerServletFilterCustom(servletContext);

		Filter[] filters = getServletFilters();
		if (!ObjectUtils.isEmpty(filters)) {
			for (Filter filter : filters) {
				registerServletFilter(servletContext, filter);
			}
		}

//		servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		servletContext.getSessionCookieConfig().setPath("/");
		servletContext.getSessionCookieConfig().setDomain(null);
		servletContext.getSessionCookieConfig().setHttpOnly(true);
//		servletContext.getSessionCookieConfig().setMaxAge(600);
//		servletContext.getSessionCookieConfig().setSecure(true);
		customizeRegistration(registration);
	}

	private void registerServletFilterCustom(ServletContext servletContext){
		FilterRegistration.Dynamic encodingFilterRegistration = servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
		checkRegister(encodingFilterRegistration, "encoding-filter");
	    encodingFilterRegistration.setInitParameter("encoding", "UTF-8");
	    encodingFilterRegistration.setInitParameter("forceEncoding", "true");
	    encodingFilterRegistration.addMappingForUrlPatterns(null, true, "/*");

	    FilterRegistration.Dynamic logFilterRegistration = servletContext.addFilter("LoggingRequestResponseFilter", new HttpServletRequestResponseLoggingFilter());
	    checkRegister(logFilterRegistration, "LoggingRequestResponseFilter");
	    logFilterRegistration.setInitParameter("useMessageSeparator", "true");
	    logFilterRegistration.addMappingForUrlPatterns(null, true, "/*");

	    DelegatingFilterProxy delegateFilterProxy = new DelegatingFilterProxy();
	    FilterRegistration.Dynamic secureFilterRegistration = servletContext.addFilter("springSecurityFilterChain", delegateFilterProxy);
	    checkRegister(secureFilterRegistration, "springSecurityFilterChain");
	    secureFilterRegistration.setAsyncSupported(true);
		secureFilterRegistration.addMappingForUrlPatterns(getSecurityDispatcherTypes(), true,"/*");
	}

	private void checkRegister(FilterRegistration.Dynamic registration, String filterName){
		if (registration == null)
			throw new IllegalStateException(
					"Duplicate Filter registration for '"+filterName+"'. Check to ensure the Filter is only configured once.");
	}


	protected Set<SessionTrackingMode> getSessionTrackingModes() {
		return EnumSet.of(SessionTrackingMode.COOKIE);
	}

	protected EnumSet<DispatcherType> getSecurityDispatcherTypes() {
		return EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR,	DispatcherType.ASYNC);
	}
}
