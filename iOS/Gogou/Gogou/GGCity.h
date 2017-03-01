//
//  GGCity.h
//  Gogou
//
//  Created by xijunli on 16/4/22.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGCity : NSObject

@property(nonatomic) NSNumber *Id;
@property(nonatomic) NSString *name;
@property(nonatomic) NSString *imagePath;
@property(nonatomic) NSString *languageCode;
//@property(nonatomic) UIImage *image;
@property(nonatomic) NSMutableArray *fileOutput;
@property(nonatomic) NSString *username;

+ (void)originListRequestWithLanguageCode:(NSString *)languageCode;
+ (void)destinationListRequestWithLanguageCode:(NSString *)languageCode;
+ (void)tripOriginListRequestWithLanguageCode:(NSString *)languageCode;
+ (void)tripDestinationListRequestWithLanguageCode:(NSString *)languageCode;
@end
