//
//  DetailPageTableViewCell3.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/28.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DetailPageTableViewCell3.h"

@implementation DetailPageTableViewCell3

- (void)awakeFromNib {
    // Initialization code
    _height = 97.0f;
    [_linkButton setTitle:NSLocalizedString(@"联系TA", nil) forState:UIControlStateNormal];
        
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}



@end
