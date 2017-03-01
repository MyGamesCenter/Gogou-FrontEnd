//
//  DemandTableViewCell.m
//  Gogou
//
//  Created by xijunli on 16/4/23.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "DemandTableViewCell.h"


@implementation DemandTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setupCell:(Demand *)demand AndImagePath:(NSString *)imagePath{

    self.productImage.image = [UIImage imageNamed:imagePath];
    self.productnameLabel.text = demand.productname;
    self.originLabel.text = demand.origin;
    self.desitinationLabel.text = demand.destination;
    self.Id = demand.Id;
}

@end
