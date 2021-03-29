package de.tobiashapp.coinbase.plans.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app.mail")
@ConstructorBinding
data class MailProperties(val sender: String)
