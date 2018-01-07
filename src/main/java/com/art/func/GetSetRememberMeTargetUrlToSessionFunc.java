package com.art.func;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GetSetRememberMeTargetUrlToSessionFunc {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public GetSetRememberMeTargetUrlToSessionFunc(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setRememberMeTargetUrlToSession(HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            session.setAttribute("targetUrl", "/admin");
        }
    }

    public String getRememberMeTargetUrlFromSession(HttpServletRequest httpServletRequest){
        String targetUrl = "";
        HttpSession session = httpServletRequest.getSession(false);
        if(session!=null){
            targetUrl = session.getAttribute("targetUrl")==null?""
                    :session.getAttribute("targetUrl").toString();
        }
        return targetUrl;
    }

}
