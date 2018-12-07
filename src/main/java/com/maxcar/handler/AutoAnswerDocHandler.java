package com.maxcar.handler;

import java.util.Map;

import com.maxcar.constants.ResponseConst;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.outxmlbuilder.NewsBuilder;
import com.soecode.wxtools.exception.WxErrorException;
/**
 * 帮助按钮
 * @author Administrator
 *
 */
public class AutoAnswerDocHandler implements WxMessageHandler {

    private static AutoAnswerDocHandler instance = null;
    NewsBuilder newsBuilder = WxXmlOutMessage.NEWS();
    private boolean isRun = false;

    private AutoAnswerDocHandler(){}

    public static synchronized AutoAnswerDocHandler getInstance(){
        if (instance == null) {
            instance = new AutoAnswerDocHandler();
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
        System.out.println("进入AutoAnswerDocHandler处理"+wxMessage);
    	WxXmlOutMessage response = null;
        if (!getIsRun()) {
            setRun(true);
            response = execute(wxMessage);
            setRun(false);
        }
        return response;
    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {
    	String back = null;
    	if("注册".equals(wxMessage.getContent())) {
    		back = ResponseConst.INSTALL;
    	}else if("礼包".equals(wxMessage.getContent())) {
    		back = ResponseConst.GIFT;
    	}else {
    		back = ResponseConst.FEEDBACKDOC;
    	}
    	return WxXmlOutMessage.TEXT().content(back).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }
}
