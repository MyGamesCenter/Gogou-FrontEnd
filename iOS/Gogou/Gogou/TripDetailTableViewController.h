//
//  TripDetailTableViewController.h
//  Gogou
//
//  Created by xijunli on 16/4/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TripDetailTableViewController : UITableViewController<UIAlertViewDelegate>

@property (nonatomic) NSNumber *tripId;
@property (nonatomic) int fromWhere;

@end

