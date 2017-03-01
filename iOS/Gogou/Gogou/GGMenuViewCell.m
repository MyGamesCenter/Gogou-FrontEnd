//
//  GGMenuViewCell.m
//  GoGou
//
//  Created by xijunli on 15/12/17.
//  Copyright © 2015年 GoGou Inc. All rights reserved.
//

#import "GGMenuViewCell.h"
#import "GoGouColors.h"

@implementation GGMenuViewCell

#pragma mark - UITableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.textLabel.textColor = [UIColor blackColor];
        self.textLabel.font = [UIFont systemFontOfSize:16.0];
        UIView *selectedBackgroundView = [UIView new];
        selectedBackgroundView.backgroundColor = SELECTED_GRAY_COLOR;
        self.selectedBackgroundView = selectedBackgroundView;
    }
    return self;
}

@end
