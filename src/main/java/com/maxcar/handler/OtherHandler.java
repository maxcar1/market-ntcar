package com.maxcar.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.WxXmlOutNewsMessage;
import com.soecode.wxtools.bean.outxmlbuilder.NewsBuilder;
import com.soecode.wxtools.exception.WxErrorException;

public class OtherHandler implements WxMessageHandler {

    private static OtherHandler instance = null;

    private boolean isRun = false;
    @Autowired
    private Environment env;
    private OtherHandler(){}

    public static synchronized OtherHandler getInstance(){
        if (instance == null) {
            instance = new OtherHandler();
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
	public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService)
			throws WxErrorException {
		System.out.println("进入扫码处理器==============");
		/*String url = env.getProperty("spring.datasource.url");
        String className = env.getProperty("spring.datasource.driver-class-name");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");*/
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	dataSource.setUrl("jdbc:MySQL://mysql-t.maxcar.com.cn:3306/maxcar_stock_l");
    	dataSource.setUsername("root");
    	dataSource.setPassword("Maxcar#2017");
    	jdbcTemplate.setDataSource(dataSource);
		NewsBuilder newsBuilder = WxXmlOutMessage.NEWS();
	            
	            System.out.println("扫码返回============="+wxMessage.getEventKey()+"===Ticket===="+wxMessage.getTicket());
	            
	            String sql = "update wish_list set user_id = '"+wxMessage.getFromUserName()+"' where ticket = '"+""+wxMessage.getTicket()+""+"'";
	            System.out.println("sql==========="+sql);
	            int flag=0;
				try {
					flag = jdbcTemplate.update(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println("flag========"+flag);
	            if(flag>=1) {
	            	WxXmlOutNewsMessage.Item item = new WxXmlOutNewsMessage.Item();
	            	item.setTitle("我的心愿单");
	            	item.setUrl("http://xfz-t.maxcar.com.cn/carWish?marketId=007&ticket="+wxMessage.getTicket()+"&userId="+wxMessage.getFromUserName()+"");
	            	item.setPicUrl("http://publish-pic-cpu.baidu.com/9489e89a-b4ad-4f3b-910f-c052755aed7e.jpeg");
	            	newsBuilder.addArticle(item);
	            	return newsBuilder.toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
	            }
	            else {
	            	return newsBuilder.toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
	            }
	            //https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2f33fc5e0cc7ae43&redirect_uri=http://47.99.83.143/wx&response_type=code&scope=snsapi_userinfo&state=007#wechat_redirect
	            // public final static String GET_WEBAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2f33fc5e0cc7ae43&secret=SECRET&code=CODE&grant_type=authorization_code";
	}

}
