//
//  PostTripTableViewController.h
//  worldshopping2.0
//
//  Created by xijunli on 16/1/2.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RESTRequestListener.h"

@interface PostTripTableViewController : UITableViewController<RESTRequestListener>
{
    NSMutableArray *categoryList;
    NSString *userName;
    NSString *subscriberId;
}

@property (nonatomic) NSMutableArray *categoryList;
@property (nonatomic) NSString *userName;
@property (nonatomic) NSString *subscriberId;

@end
