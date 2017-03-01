//
//  MyReceivedOrderViewViewController.h
//  Gogou
//
//  Created by xijunli on 16/9/14.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "ViewController.h"
#import "RESTRequestListener.h"

@interface MyReceivedOrderViewViewController : ViewController<RESTRequestListener,UITableViewDataSource,UITableViewDelegate>

@end
