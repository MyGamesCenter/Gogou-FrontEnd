//
//  DetailPageTableViewCell1.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/28.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DetailPageTableViewCell1.h"

@implementation DetailPageTableViewCell1

- (void)awakeFromNib {
    // Initialization code
    _height = 44.0f;
    //_ValueLabel.layer.borderWidth = 1.0f;
    _ValueLabel.layer.cornerRadius = 5.0f;
    _ValueLabel.layer.backgroundColor = [UIColor lightGrayColor].CGColor;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
