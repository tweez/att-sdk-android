package com.att.api.util;

import android.content.Context;

public class SdkConfig {

	public static String oAuthCode;
	public static final long noTokenExpiredTime     =   -1;
    public static final String fqdn                 =   "https://api.att.com";
	public static final String fqdn_extend          =   "/oauth/v4/authorize";
	public static final String byPassOnNetwork      =   "&custom_param=bypass_onnetwork_auth";
	public static final String byPassOnNetStr       =   "bypass_onnetwork_auth";
	public static final String suppressLandingPage  =   "&custom_param=suppress_landing_page";
	public static final String suppressLndgPageStr  =   "suppress_landing_page";
	public static final String none                 =   "none";
	public static final String byPassOnNetANDsuppressLandingPage =   
			                                            "&custom_param=bypass_onnetwork_auth,suppress_landing_page";
	public static final String byPassOnNetANDsuppressLandingStr = "bypass_onnetwork_auth,suppress_landing_page";
	public static final String preset               =   "PRESET";
	public static final String oAuthCodeStr         =   "oAuthCodeStr";
	
	
	


	

}