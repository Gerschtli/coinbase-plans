package de.tobiashapp.coinbase.plans.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender,
    @Value("\${app.mail.sender}") private val senderAddress: String,
) {
    fun sendMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom(senderAddress)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)

        mailSender.send(message)
    }
}
