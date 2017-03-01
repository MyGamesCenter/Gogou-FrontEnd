//
//  Trip.h
//  Gogou
//
//  Created by xijunli on 16/3/31.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Trip : NSObject

@property (nonatomic) NSNumber *Id;
@property (nonatomic) NSString *arrival;
@property (nonatomic) NSString *departure;
@property (nonatomic) NSString *destination;
@property (nonatomic) NSString *origin;
@property (nonatomic) NSString *descriptions;
@property (nonatomic) NSNumber *maxheight;
@property (nonatomic) NSNumber *maxlength;
@property (nonatomic) NSNumber *maxweight;
@property (nonatomic) NSNumber *maxwidth;
@property (nonatomic) NSString *userName;
@property (nonatomic) NSString *subscriberId;
@property (nonatomic) NSMutableArray *categoryNames;
@property (nonatomic) NSString *languageCode;
@property (nonatomic) NSMutableArray *fileOutputs;

@end
