package de.tobiashapp.coinbase.plans.mail

import de.tobiashapp.coinbase.plans.config.MailProperties
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
) {
    fun sendMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom(mailProperties.sender)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)

        mailSender.send(message)
    }
}
