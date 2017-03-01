//
//  CreateOrderTableViewController.h
//  Gogou
//
//  Created by xijunli on 16/4/15.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Trip.h"
#import "RESTRequestListener.h"
#import "GGCategory.h"
@interface CreateOrderTableViewController : UITableViewController<RESTRequestListener>
{
    NSMutableArray *categoryList;
}

@property (nonatomic, retain) Trip *trip;
@property (nonatomic) NSMutableArray *categoryList;

@end
