//
//  MyOrderTableViewCell1.m
//  Gogou
//
//  Created by xijunli on 16/5/11.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MyOrderTableViewCell1.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]
@implementation MyOrderTableViewCell1

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    //[self.segmentController insertSegmentWithImage:[UIImage imageNamed:@"ic_add_shopping_cart"] atIndex:0 animated:YES];
    //[self.segmentController insertSegmentWithImage:[UIImage imageNamed:@"ic_flight_takeoff"] atIndex:1 animated:YES];
    //[self.segmentController setImage:[UIImage imageNamed:@"ic_add_shopping_cart"] forSegmentAtIndex:0];
    //[self.segmentController setImage:[UIImage imageNamed:@"ic_flight_takeoff"] forSegmentAtIndex:1];
    //[segmentedControl setTintColor:[UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1]];
    [self.segmenController setTitle:NSLocalizedString(@"全部订单", nil) forSegmentAtIndex:0];
    [self.segmenController setTitle:NSLocalizedString(@"等待付款", nil) forSegmentAtIndex:1];
    [self.segmenController setTitle:NSLocalizedString(@"等待发货", nil) forSegmentAtIndex:2];
    [self.segmenController setTitle:NSLocalizedString(@"等待评价", nil) forSegmentAtIndex:3];
    [self.segmenController setTintColor:BACKGROUND_CORLOR];
    self.segmenController.selectedSegmentIndex = 0;
    //segmentedControl.segmentedControlStyle = UISegmentedControlStyleBar;
    //segmentedControl.momentary = YES; 这个是设置被点击后是否恢复原样
    self.segmenController.multipleTouchEnabled = NO;
    //self.userInteractionEnabled = NO;
    
    [self setSelectionStyle:UITableViewCellSelectionStyleNone];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
