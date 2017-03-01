//
//  MyOrderTableViewCell.h
//  Gogou
//
//  Created by xijunli on 16/4/30.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyOrderTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *buyerKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *buyerValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *sellerKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *sellerValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderStatusKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderStatusValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *productNameKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *productNameValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderDescriptionKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderDescriptionValueLabel;

@end
