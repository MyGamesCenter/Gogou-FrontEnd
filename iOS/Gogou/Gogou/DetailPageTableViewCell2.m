//
//  DetailPageTableViewCell2.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/28.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DetailPageTableViewCell2.h"

@implementation DetailPageTableViewCell2

- (void)awakeFromNib {
    // Initialization code
    _height = 128.0f;
    _detailDescriptionTextView.layer.borderWidth = 1.0f;
    //_detailDescriptionTextView.layer.backgroundColor = [UIColor lightGrayColor].CGColor;
    _detailDescriptionTextView.layer.cornerRadius = 5.0f;
    _detailDescriptionTextView.editable = NO;
    _label.text = NSLocalizedString(@"详细描述", nil);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
