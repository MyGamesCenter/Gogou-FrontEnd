//
//  DetailFirstRowCell.h
//  worldshopping2.0
//
//  Created by xijunli on 15/12/11.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailFirstRowCell : UITableViewCell
@property (strong, nonatomic) UIImageView *headPortraitImage;
@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UIImageView *star1;
@property (strong, nonatomic) UIImageView *star2;
@property (strong, nonatomic) UIImageView *star3;
@property (strong, nonatomic) UIImageView *star4;
@property (strong, nonatomic) UIImageView *star5;
@property (nonatomic) CGFloat height;

-(void) initSubview;
-(void) setupCellWithContent;

@end
