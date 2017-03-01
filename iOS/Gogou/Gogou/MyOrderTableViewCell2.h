//
//  MyOrderTableViewCell2.h
//  Gogou
//
//  Created by xijunli on 16/9/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Order.h"

@interface MyOrderTableViewCell2 : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *usernameKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *usernameValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *statesLabel;
@property (weak, nonatomic) IBOutlet UIImageView *goodsImageView;
@property (weak, nonatomic) IBOutlet UILabel *goodsNameLabel;
@property (weak, nonatomic) IBOutlet UITextView *goodsDescriptionTextView;
@property (weak, nonatomic) IBOutlet UILabel *unitLabel;
@property (weak, nonatomic) IBOutlet UILabel *quantityLabel;
@property (weak, nonatomic) IBOutlet UILabel *quantityNumberLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *minPriceValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *maxPriceValueLabel;

@property (weak, nonatomic) IBOutlet UIButton *bottomButton1;
@property (weak, nonatomic) IBOutlet UIButton *bottomButton2;


- (void)setupCellWith:(Order *)demand AndImagePath:(NSString *)imagePath AndIsDemander:(BOOL)Demander;

@end
