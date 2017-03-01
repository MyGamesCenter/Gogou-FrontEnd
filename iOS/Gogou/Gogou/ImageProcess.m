//
//  ImageProcess.m
//  Gogou
//
//  Created by xijunli on 16/4/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "ImageProcess.h"

@implementation ImageProcess

+(UIImage *)imageWithColorToButton:(UIColor *)color
{
    CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

+(UIImage *)imageConvertedFromData:(NSString *)data{
    //NSData *newData = [NSData dataWithBytes:byteData length:length];
    //NSData *nsData = [NSData dataWithBytes:byteData length:[byteData]
    //UIImage *newImage = [UIImage imageWithData:newData];
    //NSData *data = [string dataUsingEncoding:NSUTF8StringEncoding];
    //Byte *byte = (Byte *)[data bytes];
    /*NSArray *strings = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:NULL];
>>>>>>> master
    unsigned long c = strings.count;
    uint8_t *bytes = malloc(sizeof(*bytes) * c);
    for (int i = 0; i < c; i++)
    {
        NSString *str = [strings objectAtIndex:i];
        int byte = [str intValue];
        bytes[i] = byte;
<<<<<<< HEAD
    }
    
    NSData *imageData = [NSData dataWithBytesNoCopy:bytes length:c freeWhenDone:YES];
    UIImage *image = [UIImage imageWithData:imageData];
=======
    }*/
    
    /*NSData *imageData = [NSData dataWithBytesNoCopy:[data UTF8String] length:[data lengthOfBytesUsingEncoding:NSUTF8StringEncoding] freeWhenDone:NO];*/
    NSData *pictureData = [NSData data];
    pictureData = [pictureData initWithBase64EncodedString: data
                                     options:NSDataBase64DecodingIgnoreUnknownCharacters];
    UIImage *image = [UIImage imageWithData:pictureData];
    
    return image;
}

+ (UIImage *)reSizeImage:(UIImage *)image toSize:(CGSize)reSize

{
    UIGraphicsBeginImageContext(CGSizeMake(reSize.width, reSize.height));
    [image drawInRect:CGRectMake(0, 0, reSize.width, reSize.height)];
    UIImage *reSizeImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    
    return reSizeImage;
    
}

@end
