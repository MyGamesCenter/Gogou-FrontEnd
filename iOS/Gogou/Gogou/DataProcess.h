//
//  DataProcess.h
//  Gogou
//
//  Created by xijunli on 16/4/10.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DataProcess : NSObject

+ (NSString *) decimalwithFormat:(NSString *)format  floatV:(float)floatV;

+ (NSString *) cipherWithString:(NSString *)string;

+ (NSString *) decipherWithString:(NSString *)string;

@end
