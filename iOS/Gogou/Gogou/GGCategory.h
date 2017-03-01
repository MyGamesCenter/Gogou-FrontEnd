//
//  GGCategory.h
//  Gogou
//
//  Created by xijunli on 16/4/20.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface GGCategory : NSObject

@property(nonatomic) NSNumber *Id;
@property(nonatomic) NSString *name;
@property(nonatomic) NSString *imagePath;
@property(nonatomic) NSString *displayName;
@property(nonatomic) NSString *languageCode;
@property(nonatomic) UIImage *image;
@property(nonatomic) NSMutableArray *fileOutput;
@property(nonatomic) NSString *username;

@end
