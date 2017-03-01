//
//  DemandDetailTableViewController.h
//  Gogou
//
//  Created by xijunli on 16/4/9.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DemandDetailTableViewController : UITableViewController
{
    NSNumber *demandId;
    int fromWhere;//0表示来自首页的点击，1表示来自我的发布的点击
}

@property (nonatomic) NSNumber *demandId;
@property (nonatomic) int fromWhere;

@end
