package com.maxcar.constants;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OwnMsg {

	private OwnMsg(){}
	static ConcurrentLinkedQueue<String> sendMsgQueue = new ConcurrentLinkedQueue<String>();

	
}
