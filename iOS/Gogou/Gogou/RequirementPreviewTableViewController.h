//
//  RequirementPreviewTableViewController.h
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "Requirement.h"

@interface RequirementPreviewTableViewController : UITableViewController<UIAlertViewDelegate>{
    //Requirement *addRequ;
    UIImage *photo;
    //NSString *photoURL;
}

//@property (nonatomic, retain) Requirement *addRequ;
@property (nonatomic, retain) UIImage *photo;

@end
