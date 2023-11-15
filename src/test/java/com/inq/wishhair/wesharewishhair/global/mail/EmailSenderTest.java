package com.inq.wishhair.wesharewishhair.global.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import com.inq.wishhair.wesharewishhair.user.event.RefundMailSendEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@DisplayName("[EmailSender 테스트]")
class EmailSenderTest {

	private static final String AUTH_TEMPLATE = "AuthMailTemplate";
	private static final String REFUND_TEMPLATE = "RefundRequestMailTemplate";
	private static final String ADDRESS = "hello@naver.com";

	private final EmailSender emailSender;
	private final JavaMailSender mailSender;
	private final ITemplateEngine templateEngine;
	private final MimeMessage mimeMessage;

	public EmailSenderTest() {
		this.mailSender = Mockito.mock();
		this.templateEngine = Mockito.mock(ITemplateEngine.class);
		this.mimeMessage = Mockito.mock(MimeMessage.class);
		this.emailSender = new EmailSender("from", "receiver", mailSender, templateEngine);
	}

	@Test
	@DisplayName("인증 메일 발송 테스트")
	void sendAuthMail() throws MessagingException, UnsupportedEncodingException {
		//given
		given(templateEngine.process(any(String.class), any(Context.class))).willReturn(AUTH_TEMPLATE);
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);

		//when
		boolean actual = emailSender.sendAuthMail(ADDRESS, "1927");

		//then
		assertThat(actual).isTrue();
	}

	@Test
	@DisplayName("환급 요청 메일 발송 테스트")
	void sendRefundRequestMail() throws MessagingException, UnsupportedEncodingException {
		//given
		given(templateEngine.process(any(String.class), any(Context.class))).willReturn(REFUND_TEMPLATE);
		given(mailSender.createMimeMessage()).willReturn(mimeMessage);
		RefundMailSendEvent event = new RefundMailSendEvent("userA", "기업은행", "111111111", 1000);

		//when
		boolean actual = emailSender.sendRefundRequestMail(event);

		//then
		assertThat(actual).isTrue();
	}
}