package com.russel.utils.thread;

import com.russel.model.wemedia.pojos.WmUser;

/**
 * author by Russel
 * created by 2023/10/17.
 */
public class WmThreadLocalUtil {

    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(WmUser wmUser){
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    public static WmUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}
