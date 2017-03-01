//
//  OrderDetailTableViewCell1.h
//  Gogou
//
//  Created by xijunli on 16/9/22.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OrderDetailTableViewCell1 : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *orderNumberKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderNumberValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *productNameValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *brandNameValueLabel;
@property (weak, nonatomic) IBOutlet UITextView *orderDescriptionTextView;
@property (weak, nonatomic) IBOutlet UILabel *categoryKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *categoryValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *quantityKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *quantityValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *unitLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceKeyLabel;
@property (weak, nonatomic) IBOutlet UILabel *minPriceLabel;

@property (weak, nonatomic) IBOutlet UILabel *maxPriceLabel;

@end
