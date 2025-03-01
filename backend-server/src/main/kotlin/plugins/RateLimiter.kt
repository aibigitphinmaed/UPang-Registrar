package com.ite393group5.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.hours


fun Application.configureRateLimiter() {
    install(RateLimit) {
        register(RateLimitName("health-check")) {
            rateLimiter(limit = 5, refillPeriod = 1.hours)
        }
    }
}