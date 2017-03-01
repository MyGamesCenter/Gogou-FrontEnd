//
//  OrderUtils.h
//  Gogou
//
//  Created by xijunli on 16/9/12.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OrderUtils : NSObject

/***
 orderNumber comprised of date and a random int
 */
+ (NSString *)getOrderNumber;

@end
