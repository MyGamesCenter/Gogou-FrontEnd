//
//  DetailPageTableViewCell4.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "DetailPageTableViewCell4.h"

@implementation DetailPageTableViewCell4

- (void)awakeFromNib {
    // Initialization code
    _label.text = NSLocalizedString(@"图片预览", nil);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
