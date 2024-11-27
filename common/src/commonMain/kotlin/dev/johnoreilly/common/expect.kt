package dev.johnoreilly.common

import org.koin.core.module.Module

expect fun platformModule(): Module

expect fun String.format(vararg args: Any?): String
