//
//  DetailPageTableViewCell.h
//  WorldShopping
//
//  Created by xijunli on 15/11/28.
//  Copyright © 2015年 laimeng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DetailCellContent.h"

#define DetailPageCellTableViewCellLabelFontSize 15

@class DetailCellContent;

@interface DetailPageTableViewCell : UITableViewCell

@property(nonatomic,assign) CGFloat height;
@property(nonatomic,retain) UILabel* label1;
@property(nonatomic,retain) UILabel* label2;

-(instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier;
-(void)initSubView;
-(void)setCellContentWithInfo:(DetailCellContent*) dcc;

@end
