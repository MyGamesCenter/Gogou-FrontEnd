//
//  DetailPageTableViewCell2.h
//  worldshopping2.0
//
//  Created by xijunli on 15/12/28.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailPageTableViewCell2 : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *label;

@property (weak, nonatomic) IBOutlet UITextView *detailDescriptionTextView;
@property (nonatomic) CGFloat height;
@end
