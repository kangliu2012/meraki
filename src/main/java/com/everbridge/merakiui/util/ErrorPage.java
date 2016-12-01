package com.everbridge.merakiui.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by kangliu on 11/5/16.
 */
@Component
public class ErrorPage {
    public ModelAndView errorPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("nonauth");
        return model;
    }
}
