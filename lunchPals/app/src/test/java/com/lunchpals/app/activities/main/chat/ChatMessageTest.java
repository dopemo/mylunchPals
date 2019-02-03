package com.lunchpals.app.activities.main.chat;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class ChatMessageTest {

    String msg = "random message";
    String user = "user";
    ChatMessage chatMessage;
    long time;

    @Before
    public void setUp(){
        chatMessage = new ChatMessage();
        chatMessage = new ChatMessage(user, msg);
        time = new Date().getTime();
        chatMessage.setMessageText(msg);
        chatMessage.setMessageUser(user);
        chatMessage.setMessageTime(time);
    }

    @Test
    public void getMessageText() throws Exception {
        assertEquals(msg, chatMessage.getMessageText());
    }

    @Test
    public void getMessageUser() throws Exception {
        assertEquals(user, chatMessage.getMessageUser());
    }

    @Test
    public void getMessageTime() throws Exception {
        assertEquals(time, chatMessage.getMessageTime());
    }

}