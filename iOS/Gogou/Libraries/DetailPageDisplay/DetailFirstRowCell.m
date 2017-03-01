//
//  DetailFirstRowCell.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/11.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DetailFirstRowCell.h"

@interface DetailFirstRowCell(){
    CGFloat leadingSpacing;
    CGFloat starSpacing;
    CGFloat labelStarSpacing;
    CGFloat headLabelSpacing;
    CGFloat verticalSpacing;
}

@end

@implementation DetailFirstRowCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        for (id subview in [self.contentView subviews]){
            [subview removeFromSuperview];
        }
    }
    return self;
}

- (void)initSubview{
    
}

- (void)setupCellWithContent{
    CGRect cellRect = [UIScreen mainScreen].bounds;
    CGRect viewRect = CGRectMake(0, 0, cellRect.size.width, 70);
    
    [self.contentView setFrame:viewRect];
    
    
    CGFloat starImageHeigth = viewRect.size.height*0.30;
    CGFloat starImageWidth = starImageHeigth;
    
   
    CGFloat headImageHeight = viewRect.size.height * 0.8;
    CGFloat headImageWidth = headImageHeight;
    CGFloat labelWidth = viewRect.size.width * 0.2;
    CGFloat labelHeight = 0.6 * viewRect.size.height;
    starSpacing = viewRect.size.width * 0.01;
    verticalSpacing = 20.0;
    labelStarSpacing = viewRect.size.width * 0.4;
    headLabelSpacing = 10.0;
    leadingSpacing = 20.0;
    
    CGFloat headPortraitY = self.contentView.center.y - 0.5 * headImageHeight;
    CGRect headPortraitRect = CGRectMake(leadingSpacing, headPortraitY, headImageWidth, headImageHeight);
    _headPortraitImage = [[UIImageView alloc]initWithFrame:headPortraitRect];
    _headPortraitImage.image = [UIImage imageNamed:@"alluser.png"];
    
    [self.contentView addSubview:_headPortraitImage];
    
    CGFloat labelX = CGRectGetMaxX(_headPortraitImage.frame)+headLabelSpacing;
    CGFloat labelY = self.contentView.center.y - 0.5 * labelHeight;
    CGRect labelRect = CGRectMake(labelX, labelY, labelWidth, labelHeight);
    _nameLabel = [[UILabel alloc]initWithFrame:labelRect];
    _nameLabel.text = @"许乐天";
    
    [self.contentView addSubview:_nameLabel];
    
    CGFloat star5X = viewRect.size.width - leadingSpacing - starImageWidth;
    CGFloat star5Y = self.contentView.center.y - 0.5 * starImageHeigth;
    CGRect star5Rect = CGRectMake(star5X, star5Y, starImageWidth, starImageHeigth);
    _star5 = [[UIImageView alloc]initWithFrame:star5Rect];
    _star5.image = [UIImage imageNamed:@"star2.png"];
    
    [self.contentView addSubview:_star5];
    
    CGFloat star4X = star5X - starSpacing - starImageWidth;
    CGFloat star4Y = star5Y;
    CGRect star4Rect = CGRectMake(star4X, star4Y, starImageWidth, starImageHeigth);
    _star4 = [[UIImageView alloc]initWithFrame:star4Rect];
    _star4.image = [UIImage imageNamed:@"star2.png"];
    
    [self.contentView addSubview:_star4];

    CGFloat star3X = star4X - starSpacing - starImageWidth;
    CGFloat star3Y = star5Y;
    CGRect star3Rect = CGRectMake(star3X, star3Y, starImageWidth, starImageHeigth);
    _star3 = [[UIImageView alloc]initWithFrame:star3Rect];
    _star3.image = [UIImage imageNamed:@"star2.png"];
    
    [self.contentView addSubview:_star3];
    
    CGFloat star2X = star3X - starSpacing - starImageWidth;
    CGFloat star2Y = star5Y;
    CGRect star2Rect = CGRectMake(star2X, star2Y, starImageWidth, starImageHeigth);
    _star2 = [[UIImageView alloc]initWithFrame:star2Rect];
    _star2.image = [UIImage imageNamed:@"star2.png"];
    
    [self.contentView addSubview:_star2];
    
    CGFloat star1X = star2X - starSpacing - starImageWidth;
    CGFloat star1Y = star5Y;
    CGRect star1Rect = CGRectMake(star1X, star1Y, starImageWidth, starImageHeigth);
    _star1 = [[UIImageView alloc]initWithFrame:star1Rect];
    _star1.image = [UIImage imageNamed:@"star2.png"];
    
    [self.contentView addSubview:_star1];
    
    _height = viewRect.size.height;
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
