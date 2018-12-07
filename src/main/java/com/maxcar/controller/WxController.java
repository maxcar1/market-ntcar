package com.maxcar.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.maxcar.constants.MenuKey;
import com.maxcar.handler.AutoAnswerDocHandler;
import com.maxcar.handler.HelpDocHandler;
import com.maxcar.handler.OtherHandler;
import com.maxcar.handler.SubscribeHandler;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxMessageRouter;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.util.xml.XStreamTransformer;

@RestController
@RequestMapping("/wx")
public class WxController {

    private IService iService = new WxService();
   
    @Autowired
    private Environment env;
    
    @GetMapping
    public String check(String signature, String timestamp, String nonce, String echostr) {
    	System.out.println("进入检查"+echostr);
        if (iService.checkSignature(signature, timestamp, nonce, echostr)) {
            return echostr;
        }
        return null;
    }

    @PostMapping
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 创建一个路由器
        WxMessageRouter router = new WxMessageRouter(iService);
        try {
            // 微信服务器推送过来的是XML格式。
            WxXmlMessage wx = XStreamTransformer.fromXml(WxXmlMessage.class, request.getInputStream());
            System.out.println("消息：\n " + wx.toString()+"\n"+wx.getEvent());
            if(wx.getEvent()!=null&&"subscribe".equals(wx.getEvent())&&(wx.getEventKey()==null||"".equals(wx.getEventKey()))) {
            	router.rule().handler(SubscribeHandler.getInstance()).end();
            }
            if(wx.getEvent()!=null&&!"subscribe".equals(wx.getEvent())&&wx.getEventKey().contains("qrscene_")) {
            	router.rule().handler(OtherHandler.getInstance()).end();
            }
            if(wx.getEvent()!=null&&wx.getEventKey()!=null) {
            	if(WxConsts.EVT_SCAN.equals(wx.getEvent())||wx.getEventKey().contains("qrscene_")) {
                	router.rule().handler(OtherHandler.getInstance()).end();
                }
            }
            
            if(MenuKey.FEEDBACK.equals(wx.getEventKey())) {
            	router.rule().handler(AutoAnswerDocHandler.getInstance()).end();
            }
            if(WxConsts.XML_MSG_TEXT.equals(wx.getMsgType())&&wx.getContent()!=null&&("礼包".equals(wx.getContent())||"注册".equals(wx.getContent()))) {
            	router.rule().handler(AutoAnswerDocHandler.getInstance()).end();
            }else if(WxConsts.XML_MSG_TEXT.equals(wx.getMsgType())&&wx.getContent()!=null&&("365".equals(wx.getContent())||"车展".equals(wx.getContent())||"权益".equals(wx.getContent()))){
            	router.rule().handler(HelpDocHandler.getInstance()).end();
            }else {
            	router.rule().handler(AutoAnswerDocHandler.getInstance()).end();
            }
            System.out.println("EventKey：\n " + wx.getEventKey());
            //router.rule().eventKey(MenuKey.OWN_MSG).handler(RankHandler.getInstance()).end();
            /*router.rule().msgType(WxConsts.XML_MSG_TEXT).matcher(new WhoAmIMatcher()).handler(new WhoAmIHandler()).end()
                    .rule().msgType(WxConsts.XML_MSG_TEXT).handler(ConfigHander.getInstance()).end()
                    .rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.HELP).handler(HelpDocHandler.getInstance()).next()
                    
                    .rule().eventKey(MenuKey.CHANGE_NEWS).handler(ChangeNewsHandler.getInstance()).next()
                    .rule().msgType(WxConsts.XML_MSG_LOCATION).eventKey(MenuKey.HOT_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.TOP_500).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.NET_HOT_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.HUAYU_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.XINAO_SONG).handler(RankHandler.getInstance()).end();*/
            // 把消息传递给路由器进行处理
            WxXmlOutMessage xmlOutMsg = router.route(wx);
            if (xmlOutMsg != null)
                out.print(xmlOutMsg.toXml());// 因为是明文，所以不用加密，直接返回给用户。

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @RequestMapping(value="/testw",method= RequestMethod.GET)
    public String test() {
    	 String url = env.getProperty("spring.datasource.url");
         String className = env.getProperty("spring.datasource.driver-class-name");
         String username = env.getProperty("spring.datasource.username");
         String password = env.getProperty("spring.datasource.password");
 		DriverManagerDataSource dataSource = new DriverManagerDataSource();
 		JdbcTemplate jdbcTemplate = new JdbcTemplate();
     	dataSource.setDriverClassName(className);
     	dataSource.setUrl(url);
     	dataSource.setUsername(username);
     	dataSource.setPassword(password);
     	jdbcTemplate.setDataSource(dataSource);
        String sql = "select * from wish_list limit 0,1 ";
        List a = jdbcTemplate.queryForList(sql);
        System.out.println(a);
    	return "市场大屏启动";
    }
}

