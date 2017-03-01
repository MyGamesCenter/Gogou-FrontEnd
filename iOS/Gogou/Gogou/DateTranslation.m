//
//  DateTranslation.m
//  Gogou
//
//  Created by xijunli on 16/3/20.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "DateTranslation.h"

@implementation DateTranslation

+ (NSDate *) DateTranslatedFromString:(NSString *)string withLocaleIdentifier:(NSString *)localeIdentifier{
    //设置转换格式
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init] ;
    if ([localeIdentifier isEqualToString:@"en_US"])
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    else if ([localeIdentifier isEqualToString:@"zh_CN"])
        [dateFormatter setDateFormat:@"yyyy年M月d日 HH:mm"];
    NSLocale *locale = [[NSLocale alloc] initWithLocaleIdentifier:localeIdentifier];
    [dateFormatter setLocale:locale];
    //NSString转NSDate
    NSDate *date=[dateFormatter dateFromString:string];
    return date;
}
+ (NSString *) StringTranslatedFromDate:(NSDate *)date withLocaleIdentifier:(NSString *)localeIdentifier{
    //用于格式化NSDate对象
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设置格式：zzz表示时区
    if ([localeIdentifier isEqualToString:@"en_US"])
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    else if ([localeIdentifier isEqualToString:@"zh_CN"])
        [dateFormatter setDateFormat:@"yyyy年M月d日 HH:mm"];
    NSLocale *locale = [[NSLocale alloc] initWithLocaleIdentifier:localeIdentifier];
    [dateFormatter setLocale:locale];
    
    //NSDate转NSString
    NSString *string = [dateFormatter stringFromDate:date];
    return string;
}

+ (NSString *) DateFormatTranslated:(NSDate *)date withLocaleIdentifier:(NSString *)localeIdentifier{
    //用于格式化NSDate对象
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设置格式：zzz表示时区
    if ([localeIdentifier isEqualToString:@"en_US"])
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    else if ([localeIdentifier isEqualToString:@"zh_CN"])
        [dateFormatter setDateFormat:@"yyyy年M月d日"];
    NSLocale *locale = [[NSLocale alloc] initWithLocaleIdentifier:localeIdentifier];
    [dateFormatter setLocale:locale];
    
    //NSDate转NSString
    NSString *string = [dateFormatter stringFromDate:date];
    return string;
    
    
}

@end
