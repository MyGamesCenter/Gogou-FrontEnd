//
//  GoGouConstant.m
//  Gogou
//
//  Created by xijunli on 16/3/17.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "GoGouConstant.h"
#import <UIKit/UIKit.h>

#define INTERNAL_IP_ADDRESS "http://192.168.0.104:8080";
#define PUBLIC_IP_ADDRESS "http://107.167.183.80";

NSString *const REST_SERVICES_URL = @PUBLIC_IP_ADDRESS;
NSString *const SEED_VALUE = @"I LOVE GOGOU";

// preference keys
NSString *const KEY_SUBSCRIBER_ID = @"subscriber_id";
NSString *const KEY_EMAIL_ADDRESS = @"email_address";
NSString *const KEY_USER_NAME = @"username";
NSString *const KEY_PASSWORD = @"password";
NSString *const KEY_PURCHASER_FLAG = @"is_purchaser";
NSString *const KEY_USER_AGE = @"user_age";
NSString *const KEY_GENDER = @"gender";
NSString *const KEY_TOKEN = @"token";
NSString *const KEY_HEADIMAGE_PATH = @"headimage_path";
NSString *const KEY_LOGIN_TYPE = @"login_type";
NSString *const KEY_IM_TOKEN = @"im_token";
NSString *const KEY_SUBSCRIBER_INFO = @"subscriberInfoDictionary";

NSString *const KEY_OAUTH2_NICKNAME = @"oauth2_nickname";
NSString *const KEY_OAUTH2_TOKEN = @"oauth2_token";
NSString *const KEY_OAUTH2_REFRESH_TOKEN = @"oauth2_refresh_token";
NSString *const KEY_OAUTH2_INFO = @"oauth2loginDictionary";


NSString *const KEY_ORIGIN = @"origin";
NSString *const KEY_DETAIL = @"detail";
NSString *const KEY_DEPART = @"depart";
NSString *const KEY_DESTINATION = @"detination";
NSString *const KEY_ARRIVAL = @"arrival";
NSString *const KEY_ORDER_SELLER = @"seller";
NSString *const KEY_ORDER_BUYER = @"buyer";
NSString *const KEY_ORDER_DETAIL = @"order_detail";
NSString *const SENT_TOKEN_TO_SERVER = @"sentTokenToServer";
NSString *const REGISTRATION_COMPLETE = @"registrationCompelete";

// Message definitions for GCM

NSString *const GCM_SENDERID = @"1053963223593" ;
NSString *const MSG_KEY_TO_SUBSCRIBER = @"to_subscriber" ;
NSString *const MSG_KEY_FROM_SUBSCRIBER = @"from_subscriber";
NSString *const MSG_KEY_CONTENT = @"msg_content";
NSString *const MSG_KEY_ACTION = @"action";
NSString *const MSG_ACTION_MESSAGE = @"com.gogou.core.service.xmpp.MESSAGE";
NSString *const MSG_ACTION_ECHO = @"com.gogou.core.service.xmpp.ECHO";
NSString *const RONGCLOUD_IM_APPKEY = @"vnroth0krxnzo";
NSString *const WEIXIN_APP_ID = @"wxe82cd9b01d2bc379";

@implementation GoGouConstant

@end
