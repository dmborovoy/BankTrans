package com.mt940.ui.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatHelper {
    public static String format(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}
