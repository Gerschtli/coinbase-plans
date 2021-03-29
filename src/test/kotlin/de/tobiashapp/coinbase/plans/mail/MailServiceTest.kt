package de.tobiashapp.coinbase.plans.mail

import com.icegreen.greenmail.util.GreenMailUtil
import de.tobiashapp.coinbase.plans.GreenMailBaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MailServiceTest : GreenMailBaseTest() {
    @Autowired
    lateinit var mailService: MailService

    @Test
    fun `sendMessage sends mail to receiver`() {
        mailService.sendMessage(
            "receiver@mail.com",
            "Coinbase Plans Test",
            "Hello there"
        )

        val receivedMessages = greenMail.receivedMessages

        assertAll(
            { assertThat(receivedMessages).hasSize(1) },
            { assertThat(GreenMailUtil.getBody(receivedMessages[0])).isEqualTo("Hello there") },
            { assertThat(receivedMessages[0].allRecipients).hasSize(1) },
            { assertThat(receivedMessages[0].allRecipients[0].toString()).isEqualTo("receiver@mail.com") },
            { assertThat(receivedMessages[0].from).hasSize(1) },
            { assertThat(receivedMessages[0].from[0].toString()).isEqualTo("sender@mail.com") },
            { assertThat(receivedMessages[0].subject).isEqualTo("Coinbase Plans Test") },
        )
    }
}
