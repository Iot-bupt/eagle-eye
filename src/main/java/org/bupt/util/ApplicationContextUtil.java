package org.bupt.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Stack;

public class ApplicationContextUtil {

    private static volatile ApplicationContext ctx = null;

    public static void setCtx(ApplicationContext ctx) {
        ApplicationContextUtil.ctx = ctx;
    }

    public static ApplicationContext getApplicationContext() {

        if (null == ctx) {

            synchronized (ApplicationContextUtil.class) {

                if (null == ctx) {
                    ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
                }

            }

        }

        return ctx;

    }

}
