//
//  OrderStatus.h
//  Gogou
//
//  Created by xijunli on 16/9/10.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    PREORDERED,
    CONFIRMED,
    ORDERED,
    DELIVERED,
    COLLECTED,
    REFUNDED
} OrderStatus;
