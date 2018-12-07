package com.maxcar.handler;

import java.util.Map;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.WxXmlOutNewsMessage;
import com.soecode.wxtools.bean.outxmlbuilder.NewsBuilder;
import com.soecode.wxtools.exception.WxErrorException;
/**
 * 帮助按钮
 * @author Administrator
 *
 */
public class HelpDocHandler implements WxMessageHandler {

    private static HelpDocHandler instance = null;
    private boolean isRun = false;

    private HelpDocHandler(){}

    public static synchronized HelpDocHandler getInstance(){
        if (instance == null) {
            instance = new HelpDocHandler();
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
        System.out.println("进入help处理"+wxMessage);
    	WxXmlOutMessage response = null;
        if (!getIsRun()) {
            setRun(true);
            response = execute(wxMessage);
            setRun(false);
        }
        return response;
    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {
    	NewsBuilder newsBuilder = WxXmlOutMessage.NEWS();
    	if("365".equals(wxMessage.getContent())||"车展".equals(wxMessage.getContent())) {
    		WxXmlOutNewsMessage.Item item = new WxXmlOutNewsMessage.Item();
        	item.setTitle("365车展--永不落幕的国际车展");
        	item.setUrl("https://mp.weixin.qq.com/s/bqFFkoNSu78PFJHHWqNd7g");
        	item.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/nUA1rDMl5zdWOJHYXy8REmFNfdOp8KDGe1mFWmVeVPEF1GFWGSO0mgPULIoMTexvUtUnLJ8vicc3iazCicibRticetw/0?wx_fmt=jpeg");
        	newsBuilder.addArticle(item);
    	}
    	
    	if("权益".equals(wxMessage.getContent())) {
    		WxXmlOutNewsMessage.Item item = new WxXmlOutNewsMessage.Item();
        	item.setTitle("365车展会员权益");
        	item.setUrl("https://mp.weixin.qq.com/s/vNRr62oebGQ6B7A_a8PXgQ");
        	item.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/nUA1rDMl5zesJFn1AxGq4nOPGwfC383LkibydXy0icWAqDW460DwFt87DC0TH9zSda5icFElHhYjjtY2ibACn9iaPAw/0?wx_fmt=jpeg");
        	newsBuilder.addArticle(item);
    	}
    	return newsBuilder.toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    	
    	
        //return WxXmlOutMessage.TEXT().content(ResponseConst.GIFT).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }
}
