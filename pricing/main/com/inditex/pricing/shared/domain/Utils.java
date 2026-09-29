package com.inditex.pricing.shared.domain;

import com.google.common.base.CaseFormat;

public final class Utils {

    public static String toSnake(String text) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text);
    }
}
