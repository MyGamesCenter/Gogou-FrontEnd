//
//  Subscriber.h
//  Gogou
//
//  Created by xijunli on 16/3/11.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Subscriber : NSObject

@property (nonatomic) NSNumber *Id;
@property (nonatomic) NSString *emailAddress;
@property (nonatomic) NSString *encodedPassword;
@property (nonatomic) NSString *clearPassword;
@property (nonatomic) NSString *userName;
@property (nonatomic) NSNumber *isPurchaser;
@property (nonatomic) NSNumber *age;
@property (nonatomic) NSString *gender;
@property (nonatomic) NSString *lastName;
@property (nonatomic) NSString *firstName;
@property (nonatomic) NSString *gcmToken;
@property (nonatomic) NSString *imCode;
@property (nonatomic) NSString *imToken;
@property (nonatomic) NSString *regCode;

@end
