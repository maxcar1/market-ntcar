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
public class WishListHandler implements WxMessageHandler {

    private static WishListHandler instance = null;
    NewsBuilder newsBuilder = WxXmlOutMessage.NEWS();
    private boolean isRun = false;

    private WishListHandler(){}

    public static synchronized WishListHandler getInstance(){
        if (instance == null) {
            instance = new WishListHandler();
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
        System.out.println("进入WishListHandler处理"+wxMessage);
    	WxXmlOutMessage response = null;
        if (!getIsRun()) {
            setRun(true);
            response = execute(wxMessage);
            setRun(false);
        }
        return response;
    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {
    	WxXmlOutNewsMessage.Item item = new WxXmlOutNewsMessage.Item();
    	item.setUrl("https://mp.weixin.qq.com/s?__biz=MzU0NDEyMjU2Nw==&tempkey=OTgxX3BLRjcyM1ZZOSthMjlmV2w5UkpBSkpzRUROR1A2LVVGTE5nLS03MW40eDQya3cxcUlWdFp0c1pncTU5aUZ1eExlT2FVR0dza3NMVmFBb1RMQU82Z1Frd3lMSnd1YW14dmhjQ3JtNUt0d3dWeUtpUEZtV0VhZFdlRkFhb2dNRkNSeUhJY2VBQ1ZtNU0wTHJXdWY3akFxNFlMQnNyQUtYUWUwaHpDd0F%2Bfg%3D%3D&chksm=7b01b2b84c763baeda6559412e7457f6f9229c4ff164993987dd9acf7842439457e230d0a179#rd");
    	newsBuilder.addArticle(item);
    	return newsBuilder.toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }
}
