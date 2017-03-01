//
//  CacheManager.m
//  Gogou
//
//  Created by xijunli on 16/7/7.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "CacheManager.h"
#import "GoGouConstant.h"

@interface CacheManager()
{
    NSUserDefaults *_userDefaults;
}
@end

@implementation CacheManager


+ (instancetype)sharedManager
{
    static dispatch_once_t onceToken;
    static CacheManager *instance;
    dispatch_once(&onceToken, ^{
        instance = [[CacheManager alloc] init];
        instance->_userDefaults = [NSUserDefaults standardUserDefaults];
    });
    return instance;
}

- (Subscriber *) readSubscriberInfoFromLocalStorage
{
    //must initiate the _subscriber, or return a null instance
    _subscriber = [Subscriber new];
    
    if ([_userDefaults dictionaryForKey:KEY_SUBSCRIBER_INFO] != NULL)
    {
        NSDictionary *subscriberInfoDictionary = [_userDefaults dictionaryForKey:KEY_SUBSCRIBER_INFO];
        _subscriber.Id = [subscriberInfoDictionary objectForKey:KEY_SUBSCRIBER_ID];
        _subscriber.userName = [subscriberInfoDictionary objectForKey:KEY_USER_NAME];
        _subscriber.emailAddress = [subscriberInfoDictionary objectForKey:KEY_EMAIL_ADDRESS];
        _subscriber.encodedPassword = [subscriberInfoDictionary objectForKey:KEY_PASSWORD];
        _subscriber.isPurchaser = [subscriberInfoDictionary objectForKey:KEY_PURCHASER_FLAG];
        _subscriber.gender = [subscriberInfoDictionary objectForKey:KEY_GENDER];
        //_subscriber.firstName = [subscriberInfoDictionary objectForKey:@"firstName"]; // not sent by server yet
        //_subscriber.lastName = [subscriberInfoDictionary objectForKey:@"lastName"];
        _subscriber.age = [subscriberInfoDictionary objectForKey:KEY_USER_AGE];
        _subscriber.gcmToken = [subscriberInfoDictionary objectForKey:KEY_TOKEN];
        _subscriber.imToken = [subscriberInfoDictionary objectForKey:KEY_IM_TOKEN];
    }
    
    return _subscriber;
}

- (void) updateSubscriberInfoToLocalStorage:(Subscriber *)subscriber
{
    _subscriber = subscriber;
    NSDictionary *subscriberInfoDictionary = [NSDictionary dictionaryWithObjects:[NSArray arrayWithObjects:
                                                                                  //subscriber.Id,
                                                                                  subscriber.userName,
                                                                                  subscriber.emailAddress,
                                                                                  subscriber.encodedPassword,
                                                                                  subscriber.isPurchaser,
                                                                                  subscriber.gender,
                                                                                  subscriber.firstName,
                                                                                  subscriber.lastName,
                                                                                  subscriber.age,
                                                                                  subscriber.gcmToken,
                                                                                  subscriber.imToken,
                                                                                  nil]
                                              
                                                                         forKeys:[NSArray arrayWithObjects:
                                                                                  //KEY_SUBSCRIBER_ID,
                                                                                  KEY_USER_NAME,
                                                                                  KEY_EMAIL_ADDRESS,
                                                                                  KEY_PASSWORD,
                                                                                  KEY_PURCHASER_FLAG,
                                                                                  KEY_GENDER,
                                                                                  @"firstName",
                                                                                  @"lastName",
                                                                                  KEY_USER_AGE,
                                                                                  KEY_TOKEN,
                                                                                  KEY_IM_TOKEN,
                                                                                  nil]
                                              ];
    //缓存成功登陆的用户信息到本地
    [_userDefaults setObject:subscriberInfoDictionary forKey:KEY_SUBSCRIBER_INFO];
    [_userDefaults synchronize];
}

- (OAuth2Login *) readOAuth2LoginInfoFromLocalStorage
{
    
    //must initiate the _oauth2login, or return a null instance
    _oauth2Login = [OAuth2Login new];
    if ([_userDefaults dictionaryForKey:KEY_OAUTH2_INFO] != NULL)
    {
        NSDictionary *oauth2loginDictionary = [_userDefaults dictionaryForKey:KEY_OAUTH2_INFO];
        _oauth2Login.accessToken = [oauth2loginDictionary objectForKey:KEY_OAUTH2_TOKEN];
        _oauth2Login.refreshToken = [oauth2loginDictionary objectForKey:KEY_OAUTH2_REFRESH_TOKEN];
        _oauth2Login.nickname = [oauth2loginDictionary objectForKey:KEY_OAUTH2_NICKNAME];
        _oauth2Login.gender = [oauth2loginDictionary objectForKey:KEY_GENDER];
        _oauth2Login.headimgurl = [oauth2loginDictionary objectForKey:KEY_HEADIMAGE_PATH];
        //oauth2login.fileOutput = [oauth2loginDictionary objectForKey:@"fileOutput"];
    }
    
    return _oauth2Login;
}

- (void) updateOAuth2LoginInfoToLocalStorage:(OAuth2Login *)oauth2Login
{
    _oauth2Login = oauth2Login;
    NSDictionary *oauth2loginDictionary = [NSDictionary dictionaryWithObjects:[NSArray arrayWithObjects:
                                                                               oauth2Login.accessToken,
                                                                               oauth2Login.refreshToken,
                                                                               oauth2Login.nickname,
                                                                               oauth2Login.gender,
                                                                               oauth2Login.headimgurl,
                                                                               nil]
                                           
                                                                      forKeys:[NSArray arrayWithObjects:
                                                                               KEY_OAUTH2_TOKEN,
                                                                               KEY_OAUTH2_REFRESH_TOKEN,
                                                                               KEY_OAUTH2_NICKNAME,
                                                                               KEY_GENDER,
                                                                               KEY_HEADIMAGE_PATH,
                                                                               nil]
                                           ];
    //缓存成功登陆的用户信息到本地
    [_userDefaults setObject:oauth2loginDictionary forKey:KEY_OAUTH2_INFO];
    [_userDefaults synchronize];
}

- (void) updateCategoryListToLocalStorage:(NSArray *)categoryList
{
    [_categoryList addObjectsFromArray:categoryList];
}

- (BOOL) isLogin
{
    return (_subscriber.userName || _oauth2Login.nickname) ? YES : NO;
}

@end
