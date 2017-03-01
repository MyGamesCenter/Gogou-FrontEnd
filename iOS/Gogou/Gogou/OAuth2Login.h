//
//  OAuth2Login.h
//  Gogou
//
//  Created by xijunli on 16/7/28.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OAuth2Login : NSObject

@property (nonatomic) NSString *errcode;
@property (nonatomic) NSString *errmsg;
@property (nonatomic) NSString *accessToken; //-> password
@property (nonatomic) NSString *refreshToken; //-> saved as refresh token if the access token expires, we have to use refresh token to get another access token
@property (nonatomic) NSString *nickname; //-> user name
@property (nonatomic) NSString *gender; //-> gender
@property (nonatomic) NSString *headimgurl;
@property (nonatomic) NSData *fileOutput; //-> image to display in user icon


@end
