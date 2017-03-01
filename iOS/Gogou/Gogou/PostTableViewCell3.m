//
//  PostTableViewCell3.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "PostTableViewCell3.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@implementation PostTableViewCell3

- (void)awakeFromNib {
    // Initialization code
    [_submitButton setBackgroundImage:[self imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_submitButton setBackgroundImage:[self imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    [_submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    //_previewButton.layer.borderColor = [UIColor colorWithRed:57.0f/255 green:125.0f/255 blue:194.0f/255 alpha:1].CGColor;
    _submitButton.layer.borderWidth = 0;
    _submitButton.layer.cornerRadius = 5.0f;
}

-(UIImage *)imageWithColorToButton:(UIColor *)color
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

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
