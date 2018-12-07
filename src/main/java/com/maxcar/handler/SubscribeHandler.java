package com.maxcar.handler;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.maxcar.constants.ResponseConst;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;


/**
 * 帮助按钮
 * @author Administrator
 *
 */
public class SubscribeHandler implements WxMessageHandler {

    private static SubscribeHandler instance = null;
    private boolean isRun = false;
    private SubscribeHandler(){}

    public static synchronized SubscribeHandler getInstance(){
        if (instance == null) {
            instance = new SubscribeHandler();
        }
        return instance;
    }

    private synchronized  boolean getIsRun() {
        return isRun;
    }

    private synchronized void setRun(boolean run) {
        isRun = run;
    }


    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService) throws WxErrorException {
        System.out.println("进入SubscribeHandler处理"+wxMessage);
        
    	WxXmlOutMessage response = null;
        if (!getIsRun()) {
        	HttpServletResponse res = null;
            setRun(true);
            response = execute(wxMessage);
            setRun(false);
        }
        return response;
    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {
    	return WxXmlOutMessage.TEXT().content(ResponseConst.SUBSCRIBE).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();    	
    }
}
