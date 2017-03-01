//
//  TripPreviewTableViewController.h
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Trip.h"

@interface TripPreviewTableViewController : UITableViewController<UIAlertViewDelegate>{
    Trip *addTrip;
    UIImage *photo;
    NSString *photoURL;
}

@property (nonatomic, retain) Trip *addTrip;
@property (nonatomic, retain) UIImage *photo;

//@property (nonatomic) Trip *addTrip;

@end
