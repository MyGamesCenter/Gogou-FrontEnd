//
//  TripFilter.h
//  Gogou
//
//  Created by xijunli on 16/3/31.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TripFilter : NSObject

@property(nonatomic) NSString *userName;
@property(nonatomic) NSNumber *currentPage;
@property(nonatomic) NSNumber *pageSize;
@property(nonatomic) NSString *departure;
@property(nonatomic) NSString *ratingOrder;
@property(nonatomic) NSString *categoryName;
@property(nonatomic) NSString *destination;
@property(nonatomic) NSString *origin;

@end
