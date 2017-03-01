//
//  OrderFilter.h
//  Gogou
//
//  Created by xijunli on 16/5/19.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OrderStatus.h"

@interface OrderFilter : NSObject

@property (nonatomic) NSString *username;
@property (nonatomic) NSNumber *tripId;
@property (nonatomic) OrderStatus orderStatus;
@property (nonatomic) NSString *orderType;
@property (nonatomic) NSNumber *currentPage;
@property (nonatomic) NSNumber *pageSize;
@property (nonatomic) NSString *sellerId;

@end
