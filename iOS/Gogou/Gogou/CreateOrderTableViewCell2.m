//
//  CreateOrderTableViewCell2.m
//  Gogou
//
//  Created by xijunli on 16/4/15.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "CreateOrderTableViewCell2.h"

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]


@implementation CreateOrderTableViewCell2

- (void)awakeFromNib {
    // Initialization code
    [_CategoryNameValueLabel1.layer setCornerRadius:5.0f];
    [_CategoryNameValueLabel2.layer setCornerRadius:5.0f];
    [_CategoryNameValueLabel3.layer setCornerRadius:5.0f];
    
    [_CategoryNameValueLabel1 setBackgroundColor:BACKGROUND_CORLOR];
    [_CategoryNameValueLabel2 setBackgroundColor:BACKGROUND_CORLOR];
    [_CategoryNameValueLabel3 setBackgroundColor:BACKGROUND_CORLOR];
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
