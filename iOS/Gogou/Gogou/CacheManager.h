//
//  CacheManager.h
//  Gogou
//
//  Created by xijunli on 16/7/7.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Subscriber.h"
#import "OAuth2Login.h"

@interface CacheManager : NSObject

- (Subscriber *) readSubscriberInfoFromLocalStorage;

- (void) updateSubscriberInfoToLocalStorage:(Subscriber *)subscriber;

- (OAuth2Login *) readOAuth2LoginInfoFromLocalStorage;

- (void) updateOAuth2LoginInfoToLocalStorage:(OAuth2Login *)oauth2Login;



- (void) updateCategoryListToLocalStorage:(NSArray *)categoryList;

- (BOOL) isLogin;

+ (instancetype)sharedManager;

@property (strong, nonatomic) Subscriber *subscriber;
@property (strong, nonatomic) OAuth2Login *oauth2Login;
@property (strong, nonatomic) NSMutableArray *categoryList;

@end
