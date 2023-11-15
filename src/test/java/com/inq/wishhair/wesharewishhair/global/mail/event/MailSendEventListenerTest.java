package com.inq.wishhair.wesharewishhair.global.mail.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.inq.wishhair.wesharewishhair.auth.event.AuthMailSendEvent;
import com.inq.wishhair.wesharewishhair.common.support.MockTestSupport;
import com.inq.wishhair.wesharewishhair.global.mail.EmailSender;
import com.inq.wishhair.wesharewishhair.point.application.dto.PointUseRequest;
import com.inq.wishhair.wesharewishhair.user.domain.entity.Email;
import com.inq.wishhair.wesharewishhair.user.event.RefundMailSendEvent;

class MailSendEventListenerTest extends MockTestSupport {
    private static final String ADDRESS = "hello@naver.com";

    @InjectMocks
    private MailSendEventListener listener;

    @Mock
    private EmailSender emailSender;

    @Test
    @DisplayName("인증 메일 발송 이벤트 리스너 테스트")
    void sendAuthMail() throws Exception {
        //given
        final String authKey = "2816";
        given(emailSender.sendAuthMail(anyString(), anyString())).willReturn(true);

        //when, then
        assertDoesNotThrow(() -> listener.sendAuthMail(new AuthMailSendEvent(new Email(ADDRESS), authKey)));
    }

    @Test
    @DisplayName("포인트 환급요청 메일 발송 이벤트 리스너 테스트")
    void sendRefundMail() throws Exception {
        //given
        PointUseRequest request = new PointUseRequest("bank", "1234", 1000);
        RefundMailSendEvent event = request.toRefundMailEvent("userName");
        given(emailSender.sendRefundRequestMail(event)).willReturn(true);

        //when, then
        assertDoesNotThrow(() -> listener.sendRefundMail(event));
    }
}
