package com.mak.Api;

import com.mak.App;

public class WebServices {

    public static String BASE_URL =App.getPrefs().getValue("URL")+":"+App.getPrefs().getValue("PORT")+"/v1/";
}
