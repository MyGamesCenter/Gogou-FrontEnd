//
//  DateTranslation.h
//  Gogou
//
//  Created by xijunli on 16/5/4.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DateTranslation : NSObject
+ (NSDate *) DateTranslatedFromString:(NSString *)string withLocaleIdentifier:(NSString *)localeIdentifier;
+ (NSString *) StringTranslatedFromDate:(NSDate *)date withLocaleIdentifier:(NSString *)localeIdentifier;
+ (NSString *) DateFormatTranslated:(NSDate *)date withLocaleIdentifier:(NSString *)localeIdentifier;
@end
