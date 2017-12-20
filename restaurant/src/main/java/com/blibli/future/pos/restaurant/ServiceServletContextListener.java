package com.blibli.future.pos.restaurant;

import com.blibli.future.pos.restaurant.common.ApplicationUtility;
import com.blibli.future.pos.restaurant.common.model.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileReader;

public class ServiceServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String configFile = servletContext.getRealPath("/WEB-INF/classes/config.json");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(new FileReader(configFile), Config.class);
            servletContext.setAttribute("restaurantConfig", config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApplicationUtility.setServletContext(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("restaurantConfig");
    }
}
