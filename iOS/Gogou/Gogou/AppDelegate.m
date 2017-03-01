//
//  AppDelegate.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/4.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "AppDelegate.h"
#import "GoGouColors.h"
#import "GoGouConstant.h"
#import "GGMenuViewController.h"
#import "IQKeyboardManager.h"
#import "WXApiManager.h"
#import "CacheManager.h"
#import <RestKit/RestKit.h>
#import <RongIMKit/RongIMKit.h>
#import <RongIMLib/RongIMLib.h>


@interface AppDelegate () <MSDynamicsDrawerViewControllerDelegate, RCIMConnectionStatusDelegate>
{
    GGMenuViewController *_menuViewController;
}
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application willFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    return  YES;
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    
    // Read app/user saved information
    [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
    [[CacheManager sharedManager] readOAuth2LoginInfoFromLocalStorage];
    
    //初始化融云SDK
    [[RCIM sharedRCIM] initWithAppKey:RONGCLOUD_IM_APPKEY];
    
    /**
     * 推送处理1
     */
    if ([application respondsToSelector:@selector(registerUserNotificationSettings:)])
    {
        //注册推送, iOS 8
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeBadge |
                                                                                             UIUserNotificationTypeSound |
                                                                                             UIUserNotificationTypeAlert)
                                                                                 categories:nil];
        [application registerUserNotificationSettings:settings];
    } else {
        UIRemoteNotificationType myTypes =
        UIRemoteNotificationTypeBadge |
        UIRemoteNotificationTypeAlert |
        UIRemoteNotificationTypeSound;
        [application registerForRemoteNotificationTypes:myTypes];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didReceiveMessageNotification:)
                                                 name:RCKitDispatchMessageNotification
                                               object:nil];
    [[RCIM sharedRCIM] setConnectionStatusDelegate:self];
    
    self.dynamicsDrawerViewController = (MSDynamicsDrawerViewController *)self.window.rootViewController;
    self.dynamicsDrawerViewController.delegate = self;
    
    [[UINavigationBar appearance] setBarTintColor:APP_MAIN_CORLOR];
    [[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
  
    
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    
    [IQKeyboardManager sharedManager].enable = YES;

    // Add some example stylers
    [self.dynamicsDrawerViewController addStylersFromArray:@[[MSDynamicsDrawerScaleStyler styler],
                                                             [MSDynamicsDrawerFadeStyler styler]]
                                              forDirection:MSDynamicsDrawerDirectionLeft];
    
    
    _menuViewController = [GGMenuViewController new];
    [_menuViewController initialize];
    _menuViewController.dynamicsDrawerViewController = self.dynamicsDrawerViewController;
    [self.dynamicsDrawerViewController setDrawerViewController:_menuViewController forDirection:MSDynamicsDrawerDirectionLeft];
    
    // Transition to the first view controller
    [_menuViewController transitionToViewController:GGPaneViewControllerTypeMainScreen];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.rootViewController = self.dynamicsDrawerViewController;
    [self.window makeKeyAndVisible];
    [self.window addSubview:self.windowBackground];
    [self.window sendSubviewToBack:self.windowBackground];
    
    //向微信注册
    [WXApi registerApp:WEIXIN_APP_ID];
    
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
     
    return YES;
}

/**
 *  将得到的devicetoken 传给融云用于离线状态接收push ，您的app后台要上传推送证书
 *
 *  @param application <#application description#>
 *  @param deviceToken <#deviceToken description#>
 */
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    NSString *token = [[[[deviceToken description]
                         stringByReplacingOccurrencesOfString:@"<" withString:@""]
                         stringByReplacingOccurrencesOfString:@">" withString:@""]
                         stringByReplacingOccurrencesOfString:@" " withString:@""];
    
    [[RCIMClient sharedRCIMClient] setDeviceToken:token];
}


/**
 *  网络状态变化。
 *
 *  @param status 网络状态。
 */
- (void)onRCIMConnectionStatusChanged:(RCConnectionStatus)status
{
    if (status == ConnectionStatus_KICKED_OFFLINE_BY_OTHER_CLIENT)
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"提示"
                                                        message:@"您的帐号在别的设备上登录，您被迫下线！"
                                                       delegate:nil
                                              cancelButtonTitle:@"知道了"
                                              otherButtonTitles:nil, nil];
        [alert show];
        /*
        ViewController *loginVC = [[ViewController alloc] init];
        UINavigationController *_navi =
        [[UINavigationController alloc] initWithRootViewController:loginVC];
        self.window.rootViewController = _navi;*/
    }
}

- (void)didReceiveMessageNotification:(NSNotification *)notification
{
    [UIApplication sharedApplication].applicationIconBadgeNumber =
    [UIApplication sharedApplication].applicationIconBadgeNumber + 1;
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    return  [WXApi handleOpenURL:url delegate:[WXApiManager sharedManager]];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    return [WXApi handleOpenURL:url delegate:[WXApiManager sharedManager]];
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
