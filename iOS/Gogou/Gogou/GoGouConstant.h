//
//  GoGouConstant.h
//  Gogou
//
//  Created by xijunli on 16/3/17.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

extern NSString *const REST_SERVICES_URL;
extern NSString *const SEED_VALUE;
extern NSString *const KEY_SUBSCRIBER_ID;
extern NSString *const KEY_EMAIL_ADDRESS;
extern NSString *const KEY_USER_NAME;
extern NSString *const KEY_PASSWORD;
extern NSString *const KEY_TOKEN;
extern NSString *const KEY_PURCHASER_FLAG;
extern NSString *const KEY_USER_AGE;
extern NSString *const KEY_GENDER;
extern NSString *const KEY_TOKEN;
extern NSString *const KEY_HEADIMAGE_PATH;
extern NSString *const KEY_LOGIN_TYPE;
extern NSString *const KEY_IM_TOKEN;
extern NSString *const KEY_SUBSCRIBER_INFO;

extern NSString *const KEY_OAUTH2_NICKNAME;
extern NSString *const KEY_OAUTH2_TOKEN;
extern NSString *const KEY_OAUTH2_REFRESH_TOKEN;
extern NSString *const KEY_OAUTH2_INFO;


extern NSString *const KEY_ORIGIN;
extern NSString *const KEY_DETAIL;
extern NSString *const KEY_DEPART;
extern NSString *const KEY_DESTINATION;
extern NSString *const KEY_ARRIVAL;
extern NSString *const KEY_ORDER_SELLER;
extern NSString *const KEY_ORDER_BUYER;
extern NSString *const KEY_ORDER_DETAIL;
extern NSString *const SENT_TOKEN_TO_SERVER;
extern NSString *const REGISTRATION_COMPLETE;

// Message definitions for GCM

extern NSString *const GCM_SENDERID;
extern NSString *const MSG_KEY_TO_SUBSCRIBER;
extern NSString *const MSG_KEY_FROM_SUBSCRIBER;
extern NSString *const MSG_KEY_CONTENT;
extern NSString *const MSG_KEY_ACTION;
extern NSString *const MSG_ACTION_MESSAGE;
extern NSString *const MSG_ACTION_ECHO;
extern int *const MSG_REGISTRATION;
extern int *const MSG_TOPICS;
extern int *const MSG_MESSAGE;
extern NSArray *const TOPICS;
extern NSString *const RONGCLOUD_IM_APPKEY;
extern NSString *const WEIXIN_APP_ID;


@interface GoGouConstant : NSObject

@end
