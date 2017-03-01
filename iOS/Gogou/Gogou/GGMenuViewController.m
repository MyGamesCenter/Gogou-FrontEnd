//
//  GGMenuViewController.m
//  GoGou
//
//  Created by xijunli on 15/12/17.
//  Copyright © 2015年 GoGou Inc. All rights reserved.
//

#import "GoGouColors.h"
#import "GGMenuViewController.h"
#import "GGMenuTableViewHeader.h"
#import "GGMenuViewCell.h"
#import "MyPostTableViewController.h"
#import "MyMessageViewController.h"
#import "HelpViewController.h"
#import "AboutViewController.h"
#import "ViewController.h"
#import "MainScreenViewController.h"
#import "LoginViewController.h"
#import "MyOrderViewController.h"
#import "MyReceivedOrderViewViewController.h"
#import "MyCollectionTableViewController.h"
#import "MySettingViewController.h"
#import "Subscriber.h"
#import "CacheManager.h"
#import <Foundation/Foundation.h>


NSString * const GGMenuCellReuseIdentifier = @"Drawer Cell";
NSString * const GGDrawerHeaderReuseIdentifier = @"Drawer Header";

typedef NS_ENUM(NSUInteger, GGMenuViewControllerTableViewSectionType) {
    GGMenuViewControllerTableViewSectionTypeMainScreen,
    GGMenuViewControllerTableViewSectionTypeInformation,
    GGMenuViewControllerTableViewSectionTypeCount
};

@interface GGMenuViewController ()

@property (nonatomic, strong) NSDictionary *paneViewControllerTitles;

@property (nonatomic, strong) NSDictionary *paneViewControllerClasses;

@property (nonatomic, strong) NSDictionary *sectionTitles;
@property (nonatomic, strong) NSArray *tableViewSectionBreaks;

@property (nonatomic, strong) UIBarButtonItem *paneStateBarButtonItem;
@property (nonatomic, strong) UIBarButtonItem *paneRevealLeftBarButtonItem;
@property (nonatomic, strong) UIBarButtonItem *paneRevealRightBarButtonItem;

@end

@implementation GGMenuViewController

#pragma mark - NSObject

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self initialize];
    }
    return self;
}

#pragma mark - UIViewController

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self initialize];
    }
    return self;
}

- (void)loadView
{
    self.tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStyleGrouped];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.tableView registerClass:[GGMenuViewCell class] forCellReuseIdentifier:GGMenuCellReuseIdentifier];
    [self.tableView registerClass:[GGMenuTableViewHeader class] forHeaderFooterViewReuseIdentifier:GGDrawerHeaderReuseIdentifier];
    self.tableView.backgroundColor = [UIColor whiteColor];
    self.tableView.separatorColor = [UIColor colorWithWhite:1.0 alpha:0.25];
}

- (NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskAllButUpsideDown;
}

#pragma mark - MSMenuViewController

- (void)initialize
{
    //从缓存中读取用户登录信息
    Subscriber *subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
    BOOL isLogin = [[CacheManager sharedManager] isLogin];
    
    self.paneViewControllerType = NSUIntegerMax;
    self.paneViewControllerTitles = @{
                                      @(GGPaneViewControllerTypeMainScreen) : isLogin ? subscriber.userName:NSLocalizedString(@"马上登陆", nil),
                                      @(GGPaneViewControllerTypeMyPost) : NSLocalizedString(@"我的发布", nil),
                                      @(GGPaneViewControllerTypeMyOrder) : NSLocalizedString(@"我的订单", nil),
                                      @(GGPaneViewControllerTypeMyReceivedOrder) : NSLocalizedString(@"我的接单", nil),
                                      @(GGPaneViewControllerTypeMyFavorite) : NSLocalizedString(@"我的收藏", nil),
                                      @(GGPaneViewControllerTypeMyMessage) : NSLocalizedString(@"我的消息", nil),
                                      @(GGPaneViewControllerTypeSetting) : NSLocalizedString(@"我的设置", nil),
                                      @(GGPaneViewControllerTypeHelp) : NSLocalizedString(@"帮助", nil),
                                      @(GGPaneViewControllerTypeAbout) : NSLocalizedString(@"关于", nil),
                                      };
    self.paneViewControllerClasses = @{
                                       @(GGPaneViewControllerTypeMainScreen) : [MainScreenViewController class],
                                       @(GGPaneViewControllerTypeMyPost) : [MyPostTableViewController class],
                                       @(GGPaneViewControllerTypeMyOrder) : [MyOrderViewController class],
                                       @(GGPaneViewControllerTypeMyReceivedOrder) : [MyReceivedOrderViewViewController class],
                                       @(GGPaneViewControllerTypeMyFavorite) : [MyCollectionTableViewController class],
                                       @(GGPaneViewControllerTypeMyMessage) : [MyMessageViewController class],
                                       @(GGPaneViewControllerTypeSetting) : [MySettingViewController class],
                                       @(GGPaneViewControllerTypeHelp) : [HelpViewController class],
                                       @(GGPaneViewControllerTypeAbout) : [AboutViewController class],
                                       };
    self.sectionTitles = @{
                           @(GGMenuViewControllerTableViewSectionTypeMainScreen):@"",
                           @(GGMenuViewControllerTableViewSectionTypeInformation) : @"",
                           };
    
    self.tableViewSectionBreaks = @[
                                    @(GGPaneViewControllerTypeMyPost),
                                    @(GGPaneViewControllerTypeCount)
                                    ];
}

- (GGPaneViewControllerType)paneViewControllerTypeForIndexPath:(NSIndexPath *)indexPath
{
    GGPaneViewControllerType paneViewControllerType;
    if (indexPath.section == 0) {
        paneViewControllerType = indexPath.row;
    } else {
        paneViewControllerType = ([self.tableViewSectionBreaks[(indexPath.section - 1)] integerValue] + indexPath.row);
    }
    NSAssert(paneViewControllerType < GGPaneViewControllerTypeCount, @"Invalid Index Path");
    return paneViewControllerType;
}

- (void)transitionToViewController:(GGPaneViewControllerType)paneViewControllerType
{
    // Close pane if already displaying the pane view controller
    if (paneViewControllerType == self.paneViewControllerType) {
        [self.dynamicsDrawerViewController setPaneState:MSDynamicsDrawerPaneStateClosed animated:YES allowUserInterruption:YES completion:nil];
        return;
    }
    
    BOOL animateTransition = self.dynamicsDrawerViewController.paneViewController != nil;
    
    Class paneViewControllerClass = self.paneViewControllerClasses[@(paneViewControllerType)];
    UIViewController *paneViewController = (UIViewController *)[paneViewControllerClass new];
    
    
    paneViewController.navigationItem.title = self.paneViewControllerTitles[@(paneViewControllerType)];
    
    self.paneRevealLeftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"backward"] style:UIBarButtonItemStyleBordered target:self action:@selector(dynamicsDrawerRevealLeftBarButtonItemTapped:)];
    paneViewController.navigationItem.leftBarButtonItem = self.paneRevealLeftBarButtonItem;
    
    /*self.paneRevealRightBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"Right Reveal Icon"] style:UIBarButtonItemStyleBordered target:self action:@selector(dynamicsDrawerRevealRightBarButtonItemTapped:)];
    paneViewController.navigationItem.rightBarButtonItem = self.paneRevealRightBarButtonItem;*/
    
    UINavigationController *paneNavigationViewController = [[UINavigationController alloc] initWithRootViewController:paneViewController];
    [self.dynamicsDrawerViewController setPaneViewController:paneNavigationViewController animated:animateTransition completion:nil];
    
    self.paneViewControllerType = paneViewControllerType;
}

- (void)dynamicsDrawerRevealLeftBarButtonItemTapped:(id)sender
{
    [self.dynamicsDrawerViewController setPaneState:MSDynamicsDrawerPaneStateOpen inDirection:MSDynamicsDrawerDirectionLeft animated:YES allowUserInterruption:YES completion:nil];
}

- (void)dynamicsDrawerRevealRightBarButtonItemTapped:(id)sender
{
    [self.dynamicsDrawerViewController setPaneState:MSDynamicsDrawerPaneStateOpen inDirection:MSDynamicsDrawerDirectionRight animated:YES allowUserInterruption:YES completion:nil];
}

#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return GGMenuViewControllerTableViewSectionTypeCount;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return [self.tableViewSectionBreaks[section] integerValue];
    } else {
        return ([self.tableViewSectionBreaks[section] integerValue] - [self.tableViewSectionBreaks[(section - 1)] integerValue]);
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UITableViewHeaderFooterView *headerView = [self.tableView dequeueReusableHeaderFooterViewWithIdentifier:GGDrawerHeaderReuseIdentifier];
    headerView.textLabel.text = [self.sectionTitles[@(section)] uppercaseString];
    headerView.backgroundColor = APP_MAIN_CORLOR;
    return headerView;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 0)
        return 0.1;
    else
        return 10.0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return FLT_EPSILON;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
        return 64.0;
    else
        return 56.0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:GGMenuCellReuseIdentifier forIndexPath:indexPath];
    cell.textLabel.text = self.paneViewControllerTitles[@([self paneViewControllerTypeForIndexPath:indexPath])];
    BOOL isLogin = [[CacheManager sharedManager] isLogin];
    
    if (indexPath.section == 0 && indexPath.row == 0)
    {
        [cell.imageView setImage:[UIImage imageNamed: isLogin ? @"user" : @"user_gray"]];
        cell.backgroundColor = APP_MAIN_CORLOR;
    }
    if (indexPath.section == 1 && indexPath.row == 0)
        [cell.imageView setImage:[UIImage imageNamed:@"publish"]];
    if (indexPath.section == 1 && indexPath.row == 1)
        [cell.imageView setImage:[UIImage imageNamed:@"sign_yuan"]];
    if (indexPath.section == 1 && indexPath.row == 2)
        [cell.imageView setImage:[UIImage imageNamed:@"order"]];
    if (indexPath.section == 1 && indexPath.row == 3)
        [cell.imageView setImage:[UIImage imageNamed:@"favorite"]];
    if (indexPath.section == 1 && indexPath.row == 4)
        [cell.imageView setImage:[UIImage imageNamed:@"chat"]];
    if (indexPath.section == 1 && indexPath.row == 5)
        [cell.imageView setImage:[UIImage imageNamed:@"gear"]];
    if (indexPath.section == 1 && indexPath.row == 6)
        [cell.imageView setImage:[UIImage imageNamed:@"books"]];
    if (indexPath.section == 1 && indexPath.row == 7)
        [cell.imageView setImage:[UIImage imageNamed:@"info"]];
    
    return cell;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    BOOL isLogin = [[CacheManager sharedManager] isLogin];
    if (isLogin)
    {
        GGPaneViewControllerType paneViewControllerType = [self paneViewControllerTypeForIndexPath:indexPath];
        [self transitionToViewController:paneViewControllerType];
        
        // Prevent visual display bug with cell dividers
        [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
        double delayInSeconds = 0.3;
        dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayInSeconds * NSEC_PER_SEC));
        dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
            [self.tableView reloadData];
        });
    }
    else{
        UIAlertView *mBoxView = [[UIAlertView alloc]
                                 initWithTitle:@"您还未登录！"
                                 message:@"登陆后，方可获取详情"
                                 delegate:self
                                 cancelButtonTitle:@"取消"
                                 otherButtonTitles:@"登录", nil];
        [mBoxView show];
        
    }
    
}
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex{
    if (buttonIndex == 1)
    {
        LoginViewController *lvc = [LoginViewController new];
        [self presentViewController:lvc animated:YES completion:^{}];
    }

}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

@end
