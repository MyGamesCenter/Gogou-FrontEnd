//
//  PostTableViewCell1.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/2.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "PostTableViewCell1.h"

@implementation PostTableViewCell1

- (void)awakeFromNib {
    // Initialization code
    _valueTextView.layer.borderWidth = 1.0f;
    _valueTextView.layer.borderColor = [UIColor lightGrayColor].CGColor;
    _label.text = NSLocalizedString(@"详细描述", nil);

}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
