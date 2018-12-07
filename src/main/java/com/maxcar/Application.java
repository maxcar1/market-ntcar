package com.maxcar;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.maxcar.constants.MenuKey;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxMenu;
import com.soecode.wxtools.exception.WxErrorException;

@SpringBootApplication
public class Application {
	
    
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		Properties prop = new Properties();
        try {
			prop.load(Application.class.getResourceAsStream("/application.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        IService iService = new WxService();
        WxMenu menu = new WxMenu();
        List<WxMenu.WxMenuButton> btnList = new ArrayList<>();

        //飙升功能
        WxMenu.WxMenuButton btn1 = new WxMenu.WxMenuButton();
        btn1.setName("注册有礼");
        btn1.setType(WxConsts.MENU_BUTTON_VIEW);
        btn1.setUrl("https://mp.weixin.qq.com/s/KPPYUABZW0eBrZITm4t-NQ");
        
        WxMenu.WxMenuButton btn2 = new WxMenu.WxMenuButton();
        /*btn2.setType(WxConsts.MENU_BUTTON_CLICK);
        btn2.setKey(MenuKey.LIST_NAME);*/
        btn2.setName("汽车商城");
        List<WxMenu.WxMenuButton> subList2 = new ArrayList<>();
        WxMenu.WxMenuButton btn2_1 = new WxMenu.WxMenuButton();
        btn2_1.setType(WxConsts.MENU_BUTTON_VIEW);
        btn2_1.setKey(MenuKey.TWO_CAR_SHOP);
        btn2_1.setName("二手车商城");
    //btn2_1.setUrl("http://www.maxcar.com.cn/vux-customer/?market=007&customer=1#/");
        btn2_1.setUrl("http://xfz.maxcar.com.cn/?market=007&code=011jpbAj2hwT9D0SOyAj2wg5Aj2jpbAd&state=007");
        
        String onlineurl = prop.getProperty("onlineurl");
        String appId = prop.getProperty("wx.appId");
        onlineurl = URLEncoder.encode(onlineurl);
        WxMenu.WxMenuButton btn2_3 = new WxMenu.WxMenuButton();
        btn2_3.setType(WxConsts.MENU_BUTTON_VIEW);
        //https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387&redirect_uri=
        btn2_3.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+onlineurl+"&response_type=code&scope=snsapi_base&state=007#wechat_redirect");
        btn2_3.setName("我的心愿单");
        btn2_3.setKey(MenuKey.WISH_LIST);
        WxMenu.WxMenuButton btn2_4 = new WxMenu.WxMenuButton();
        btn2_4.setType(WxConsts.MENU_BUTTON_VIEW);
        btn2_4.setKey(MenuKey.CAR_FEE);
        btn2_4.setName("停车缴费");
        btn2_4.setUrl("http://www.365chezhan.cn/wap/#/parkingPay");
        
        WxMenu.WxMenuButton btn3 = new WxMenu.WxMenuButton();
        /*btn2.setType(WxConsts.MENU_BUTTON_CLICK);
        btn2.setKey(MenuKey.LIST_NAME);*/
        btn3.setName("会员中心");
        btn3.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3.setUrl("http://www.365chezhan.cn/wap/#/me");
        subList2.addAll(Arrays.asList(btn2_1, btn2_3,btn2_4));
        btn2.setSub_button(subList2);
        //将三个按钮设置进btnList
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        //设置进菜单类
        menu.setButton(btnList);
        //调用API即可
        try {
            //参数1--menu  ，参数2--是否是个性化定制。如果是个性化菜单栏，需要设置MenuRule
            iService.createMenu(menu, false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    
    
	}
}

