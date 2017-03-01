//
//  Demand.h
//  Gogou
//
//  Created by xijunli on 16/3/31.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Demand : NSObject

@property (nonatomic) NSNumber *Id;
@property (nonatomic) NSString *expectTime;
@property (nonatomic) NSString *productname;
@property (nonatomic) NSString *subscriberId;
@property (nonatomic) NSString *userName;
@property (nonatomic) NSNumber *quantity;
@property (nonatomic) NSString *brand;
@property (nonatomic) NSNumber *serviceFee;
@property (nonatomic) NSString *destination;
@property (nonatomic) NSString *origin;
@property (nonatomic) NSString *categoryName;
@property (nonatomic) NSString *languageCode;
@property (nonatomic) NSString *descriptions;
@property (nonatomic) NSMutableArray *fileOutputs;
@end
