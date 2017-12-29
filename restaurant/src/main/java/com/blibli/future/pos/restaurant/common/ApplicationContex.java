package com.blibli.future.pos.restaurant.common;

import javax.servlet.ServletContext;

public class ApplicationContex {
    private static ServletContext context;
    public static void setServletContext(ServletContext context){
        ApplicationContex.context = context;
    }
    public static ServletContext getServletContext() {
        return ApplicationContex.context;
    }
}
