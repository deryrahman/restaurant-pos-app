package com.blibli.future.pos.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * Created by dery on 11/13/17.
 */
public class WebServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String configFile = servletContext.getRealPath("/WEB-INF/classes/config.json");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(new File(configFile), Config.class);
            servletContext.setAttribute("webappConfig", config);
            System.out.println(config.getBaseUrl());
            System.out.println(config.getRestaurantRestPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("webappConfig");
    }
}
