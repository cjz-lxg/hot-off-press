package com.russel.utils.thread;

import com.russel.model.user.pojos.ApUser;

/**
 * @author Russel
 * @DATE 2023/11/2.
 */
public class AppThreadLocalUtil {

    private final static ThreadLocal<ApUser> APP_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(ApUser apUser){
        APP_USER_THREAD_LOCAL.set(apUser);
    }

    public static ApUser getUser(){
        return APP_USER_THREAD_LOCAL.get();
    }

    public static void clear(){
        APP_USER_THREAD_LOCAL.remove();
    }

}
