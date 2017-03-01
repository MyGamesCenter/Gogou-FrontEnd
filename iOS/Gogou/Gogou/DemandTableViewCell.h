//
//  DemandTableViewCell.h
//  Gogou
//
//  Created by xijunli on 16/4/23.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Demand.h"

@interface DemandTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *productnameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *productImage;
@property (weak, nonatomic) IBOutlet UILabel *originLabel;
@property (weak, nonatomic) IBOutlet UILabel *desitinationLabel;
@property (nonatomic) NSNumber* Id;

- (void) setupCell:(Demand *)demand AndImagePath:(NSString *)imagePath;
@end
