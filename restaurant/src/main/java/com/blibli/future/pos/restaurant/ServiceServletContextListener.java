package com.blibli.future.pos.restaurant;

import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileReader;

public class ServiceServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String configFile = servletContext.getRealPath("/WEB-INF/classes/config.json");

        Gson gson = new Gson();
        try {
            Config config = gson.fromJson(new FileReader(configFile), Config.class);
            servletContext.setAttribute("restaurantConfig", config);
            MySQLUtility.setDataSource(config);
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
