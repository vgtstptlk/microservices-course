package com.vgtstptlk.services;

import com.vgtstptlk.services.service.MySoapService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {
    @Autowired
    private Bus bus;

    @Bean
    public ServletRegistrationBean<CXFServlet> wsDispatcherServlet(){
        CXFServlet myCxfServlet = new CXFServlet();
        return new ServletRegistrationBean<CXFServlet>(myCxfServlet, "/ws/*");
    }

    @Bean
    public Endpoint endpoint(){
        EndpointImpl e = new EndpointImpl(bus, new MySoapService());
        e.publish("/test");
        return e;
    }
}
