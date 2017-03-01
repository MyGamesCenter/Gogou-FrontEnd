//
//  RegisterInfo.h
//  Gogou
//
//  Created by xijunli on 16/3/11.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RegisterInfo : NSObject

@property (nonatomic) NSString *emailAddress;
@property (nonatomic) NSString *encodedPassword;
@property (nonatomic) NSString *clearPassword;
@property (nonatomic) NSString *userName;
@property (nonatomic) bool isPurchaser;
@property (nonatomic) int age;
@property (nonatomic) NSString *firstName;
@property (nonatomic) NSString *lastName;
@property (nonatomic) NSString *gender;
@property (nonatomic) NSString *gcmToken;
@property (nonatomic) NSData *fileOutput;



@end
