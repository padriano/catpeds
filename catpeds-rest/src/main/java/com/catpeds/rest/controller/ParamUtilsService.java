package com.catpeds.rest.controller;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

@Service
class ParamUtilsService {

    String escapeParam(String param) {
        return Strings.isNullOrEmpty(param) ? null : param.replaceAll("[\n|\r|\t]", "_");
    }
}
