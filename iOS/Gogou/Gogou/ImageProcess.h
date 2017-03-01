//
//  ImageProcess.h
//  Gogou
//
//  Created by xijunli on 16/4/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface ImageProcess : NSObject

+(UIImage *)imageWithColorToButton:(UIColor *)color;

+(UIImage *)imageConvertedFromData:(NSString *)data;

+(UIImage *)reSizeImage:(UIImage *)image toSize:(CGSize)reSize;

@end
