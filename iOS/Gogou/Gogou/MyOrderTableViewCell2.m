//
//  MyOrderTableViewCell2.m
//  Gogou
//
//  Created by xijunli on 16/9/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MyOrderTableViewCell2.h"
#import "OrderDescription.h"

@implementation MyOrderTableViewCell2

/*
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
 
 */

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code

    
    [_bottomButton1.layer setBorderWidth:0.5];
    [_bottomButton1.layer setBorderColor:[UIColor blueColor].CGColor];
    [_bottomButton1.layer setCornerRadius:0.3];
    //_bottomButton1.tintColor = BACKGROUND_CORLOR;
    [_bottomButton2.layer setBorderWidth:0.5];
    [_bottomButton2.layer setBorderColor:[UIColor blueColor].CGColor];
    [_bottomButton2.layer setCornerRadius:0.3];
    //_bottomButton2.tintColor = BACKGROUND_CORLOR;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setupCellWith:(Order *)order AndImagePath:(NSString *)imagePath AndIsDemander:(BOOL)Demander{
    
    OrderDescription *orderDescription = order.orderDescriptions[0];
    _usernameKeyLabel.text = Demander? @"代购人" : @"下单人";
    _usernameValueLabel.text = Demander? order.sellerId : order.username;
    _goodsImageView.image = [UIImage imageNamed:imagePath];
    _goodsNameLabel.text = orderDescription.productName;
    _goodsDescriptionTextView.text = orderDescription.descriptions;
    _unitLabel.text = @"件";
    _quantityNumberLabel.text = [NSString stringWithFormat:@"%@", orderDescription.quantity];
    _minPriceValueLabel.text = [NSString stringWithFormat:@"%@", orderDescription.minPrice];
    _maxPriceValueLabel.text = [NSString stringWithFormat:@"%@", orderDescription.maxPrice];
    
    if (Demander){
        switch (order.orderStatus) {
            case PREORDERED:
                _statesLabel.text = @"等待确认";
                [_bottomButton1 setTitle:@"取消订单" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:NO];
                break;
            case CONFIRMED:
                _statesLabel.text = @"等待付款";
                [_bottomButton1 setTitle:@"取消订单" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:YES];
                [_bottomButton2 setHidden:NO];
                break;
            case ORDERED:
                _statesLabel.text = @"等待发货";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:NO];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            case DELIVERED:
                _statesLabel.text = @"等待收货";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            case COLLECTED:
                _statesLabel.text = @"等待评价";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:YES];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            default:
                break;
        }
    }
    else{
        switch (order.orderStatus) {
            case PREORDERED:
                _statesLabel.text = @"等待确认";
                [_bottomButton1 setTitle:@"确认收单" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            case CONFIRMED:
                _statesLabel.text = @"等待付款";
                [_bottomButton1 setTitle:@"确认发货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:NO];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:YES];
                [_bottomButton2 setHidden:YES];
                break;
            case ORDERED:
                _statesLabel.text = @"等待发货";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:NO];
                [_bottomButton1 setHidden:NO];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            case DELIVERED:
                _statesLabel.text = @"等待评价";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:NO];
                [_bottomButton1 setHidden:YES];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            case COLLECTED:
                _statesLabel.text = @"";
                [_bottomButton1 setTitle:@"确认收货" forState:UIControlStateNormal];
                [_bottomButton1 setEnabled:YES];
                [_bottomButton1 setHidden:YES];
                [_bottomButton2 setTitle:@"付款" forState:UIControlStateNormal];
                [_bottomButton2 setEnabled:NO];
                [_bottomButton2 setHidden:YES];
                break;
            default:
                break;
        }
    }
}


@end
