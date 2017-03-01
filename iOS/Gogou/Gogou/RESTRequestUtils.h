//
//  RESTRequestUtils.h
//  Gogou
//
//  Created by letian xu on 2016-07-04.
//  Copyright © 2016 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RESTRequestListener.h"
#import "Subscriber.h"
#import "LoginInfo.h"
#import "Trip.h"
#import "Demand.h"
#import "OAuth2Login.h"
#import "GGCategory.h"
#import "Order.h"
#import "OrderFilter.h"

@interface RESTRequestUtils : NSObject

+ (void)performGetRegCodeRequest:(NSString *)username
                    emailAddress:(NSString *)emailAddress
                        delegate:(id<RESTRequestListener>)delegate;

+ (void)performOAuth2LoginWith:(NSString *)code
                      delegate:(id<RESTRequestListener>)delegate;

+ (void)performCheckAccessTokenWith:(NSString *)savedToken
                           delegate:(id<RESTRequestListener>)delegate;

+ (void)performRefreshAccessTokenWith:(NSString *)refreshToken
                           delegate:(id<RESTRequestListener>)delegate;

+ (void)performLoginRequestWith:(LoginInfo *)loginInfo
                       delegate:(id<RESTRequestListener>)delegate;

+ (void)performGetRecoverCodeRequest:(NSString *)username
                        emailAddress:(NSString *)emailAddress
                            delegate:(id<RESTRequestListener>)delegate;

+ (void)performCreateSubscriberRequest:(Subscriber *)subscriber
                              delegate:(id<RESTRequestListener>)delegate;

+ (void)performUpdateSubsriberRequest:(Subscriber *)subscriber
                             delegate:(id<RESTRequestListener>)delegate;

+ (void)performCreateOrderRequest:(Order *) order
                       subscriber:(Subscriber *) subscriber
                         delegate:(id<RESTRequestListener>)delegate;

+ (void)performPostTripRequest:(Trip *)trip
                    imageArray:(NSMutableArray *) imageArray
                    subscriber:(Subscriber *) subscriber
                      delegate:(id<RESTRequestListener>)delegate;

+ (void)performPostDemandRequest:(Demand *)demand
                      imageArray:(NSMutableArray *) imageArray
                      subscriber:(Subscriber *) subscriber
                        delegate:(id<RESTRequestListener>)delegate;

+ (void)performGetCategoryListRequest:(NSString *)languageCode
                             delegate:(id<RESTRequestListener>)delegate;

+ (void)performGetOrderListRequest:(OrderFilter *)orderFilter
                        subscriber:(Subscriber *) subscriber
                          delegate:(id<RESTRequestListener>)delegate;

+ (void)performGetOrderRequest:(NSNumber *)orderId
                      delegate:(id<RESTRequestListener>)delegate;

+ (void)performUpdateOrderRequest:(Order *) order
                       subscriber:(Subscriber *) subscriber
                         delegate:(id<RESTRequestListener>)delegate;

@end
