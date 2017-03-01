//
//  OrderUtils.m
//  Gogou
//
//  Created by xijunli on 16/9/12.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "OrderUtils.h"

@implementation OrderUtils

+ (NSString *)getOrderNumber{
    
    // 获取系统当前时间
    NSDate * date = [NSDate date];
    NSTimeInterval sec = [date timeIntervalSinceNow];
    NSDate * currentDate = [[NSDate alloc] initWithTimeIntervalSinceNow:sec];
    
    //设置时间输出格式：
    NSDateFormatter * dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyyMMddHHmmss"];
    NSString *dateString= [dateFormatter stringFromDate:currentDate];
    
    int rand = arc4random();
    NSString *randString = [[NSString alloc]initWithFormat:@"%d",rand];
    randString = [randString substringWithRange:NSMakeRange(0, 5)];
    
    NSString *ret = [[NSString alloc]initWithFormat:@"%@%@",dateString,randString];
    
    return ret;

}

@end
