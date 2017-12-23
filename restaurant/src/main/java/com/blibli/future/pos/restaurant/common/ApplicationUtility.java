package com.blibli.future.pos.restaurant.common;

import javax.servlet.ServletContext;

public class ApplicationUtility {
    private static ServletContext context;
    public static void setServletContext(ServletContext context){
        ApplicationUtility.context = context;
    }
    public static ServletContext getServletContext() {
        return ApplicationUtility.context;
    }
}
