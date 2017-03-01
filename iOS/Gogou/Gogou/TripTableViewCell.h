//
//  TripTableViewCell.h
//  Gogou
//
//  Created by xijunli on 16/4/23.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Trip.h"

@interface TripTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *originLabel;
@property (weak, nonatomic) IBOutlet UILabel *destinationLabel;
@property (weak, nonatomic) IBOutlet UILabel *departureLabel;
@property (weak, nonatomic) IBOutlet UILabel *arrivalLabel;
@property (nonatomic) NSNumber* Id;
@property (weak, nonatomic) IBOutlet UIImageView *arrowImageView;

-(void)setupCell:(Trip *)model;
@end
