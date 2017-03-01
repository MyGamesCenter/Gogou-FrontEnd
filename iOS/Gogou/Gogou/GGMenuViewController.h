//
//  GGMenuViewController.h
//  GoGou
//
//  Created by xijunli on 15/12/17.
//  Copyright © 2015年 GoGou Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MSDynamicsDrawerViewController.h"

typedef NS_ENUM(NSUInteger, GGPaneViewControllerType) {
    GGPaneViewControllerTypeMainScreen,
    GGPaneViewControllerTypeMyPost,
    GGPaneViewControllerTypeMyOrder,
    GGPaneViewControllerTypeMyReceivedOrder,
    GGPaneViewControllerTypeMyFavorite,
    GGPaneViewControllerTypeMyMessage,
    GGPaneViewControllerTypeSetting,
    GGPaneViewControllerTypeHelp,
    GGPaneViewControllerTypeAbout,
    GGPaneViewControllerTypeCount
};

@interface GGMenuViewController : UITableViewController<UIAlertViewDelegate>

@property (nonatomic, assign) GGPaneViewControllerType paneViewControllerType;
@property (nonatomic, weak) MSDynamicsDrawerViewController *dynamicsDrawerViewController;

- (void)transitionToViewController:(GGPaneViewControllerType)paneViewControllerType;

- (void)initialize;


@end
