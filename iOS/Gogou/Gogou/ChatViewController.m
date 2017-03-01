//
//  ChatViewController.m
//  GoGou
//
//  Created by xijunli on 15/12/17.
//  Copyright © 2015年 GoGou Inc. All rights reserved.
//

#import "ChatViewController.h"
#import "DropDownMenu.h"
#import "IQKeyboardManager.h"

@interface ChatViewController () <AMBubbleTableDataSource, AMBubbleTableDelegate>

@property (nonatomic, strong) NSMutableArray* data;
@property (nonatomic, strong) DropDownMenu *dropDownMenuView;

@end

@implementation ChatViewController

//@synthesize chatFriend;

- (void)viewDidLoad
{
    // Bubble Table setup
    //[IQKeyboardManager sharedManager].enable = NO;
    //NSLog(@"开始和%@聊天", chatFriend.userName);
    
    [self setDataSource:self];
    [self setDelegate:self];
    
    [self setTitle:@"聊天"];
    
    // Dummy data
    self.data = [[NSMutableArray alloc] initWithArray:@[
                                                        
                                                        @{
                                                            @"text": @"He felt that his whole life was some kind of dream and he sometimes wondered whose it was and whether they were enjoying it.",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellReceived),
                                                            @"username": @"Stevie",
                                                            @"color": [UIColor redColor]
                                                            },
                                                        @{
                                                            @"text": @"My dad isn’t famous. My dad plays jazz. You can’t get famous playing jazz",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellSent)
                                                            },
                                                        @{
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellTimestamp)
                                                            },
                                                        @{
                                                            @"text": @"I'd far rather be happy than right any day.",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellReceived),
                                                            @"username": @"John",
                                                            @"color": [UIColor orangeColor]
                                                            },
                                                        @{
                                                            @"text": @"The only reason for walking into the jaws of Death is so's you can steal His gold teeth.",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellSent)
                                                            },
                                                        @{
                                                            @"text": @"The gods had a habit of going round to atheists' houses and smashing their windows.",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellReceived),
                                                            @"username": @"Jimi",
                                                            @"color": [UIColor blueColor]
                                                            },
                                                        @{
                                                            @"text": @"you are lucky. Your friend is going to meet Bel-Shamharoth. You will only die.",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellSent)
                                                            },
                                                        @{
                                                            @"text": @"Guess the quotes!",
                                                            @"date": [NSDate date],
                                                            @"type": @(AMBubbleCellSent)
                                                            },
                                                        
                                                        ]
                 ];
    
    // Set a style
    [self setTableStyle:AMBubbleTableStyleFlat];
    
    [self setBubbleTableOptions:@{AMOptionsBubbleDetectionType: @(UIDataDetectorTypeAll),
                                  AMOptionsBubblePressEnabled: @NO,
                                  AMOptionsBubbleSwipeEnabled: @NO,
                                  AMOptionsButtonTextColor: [UIColor colorWithRed:1.0f green:1.0f blue:184.0f/256 alpha:1.0f]}];
    
    // Call super after setting up the options
    [super viewDidLoad];
    
    [self.tableView setContentInset:UIEdgeInsetsMake(20, 0, 0, 0)];
    
    //设置按钮图标
    UIImage *rightButtonIcon = [[UIImage imageNamed:@"ic_list"]
                                imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem *rightButton = [[UIBarButtonItem alloc] initWithImage:rightButtonIcon
                                                                    style:UIBarButtonItemStylePlain
                                                                   target:self
                                                                   action:@selector(dropDown)];
    self.navigationItem.rightBarButtonItem = rightButton;
    
    CGFloat menuWidth = 140;
    CGFloat menuRowHeight = 40;
    CGFloat menuX = self.view.frame.size.width - menuWidth - 5 ;
    CGFloat menuY = CGRectGetMaxY(self.navigationController.navigationBar.frame);
    
    _dropDownMenuView = [DropDownMenu alloc];
    _dropDownMenuView = [_dropDownMenuView initWithFrame:CGRectMake(menuX, menuY, menuWidth, menuRowHeight)];
    
    [self.view addSubview:_dropDownMenuView];
    
}

- (void) dropDown{
    [_dropDownMenuView dropDown];
}

- (void)swipedCellAtIndexPath:(NSIndexPath *)indexPath withFrame:(CGRect)frame andDirection:(UISwipeGestureRecognizerDirection)direction
{
    NSLog(@"swiped");
}

#pragma mark - AMBubbleTableDataSource

- (NSInteger)numberOfRows
{
    return self.data.count;
}

- (AMBubbleCellType)cellTypeForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self.data[indexPath.row][@"type"] intValue];
}

- (NSString *)textForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return self.data[indexPath.row][@"text"];
}

- (NSDate *)timestampForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [NSDate date];
}

- (UIImage*)avatarForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([self.data[indexPath.row][@"type"] intValue] ==  AMBubbleCellSent){
        return [UIImage imageNamed:@"avatar"];
    }
    else
    {
        return [UIImage imageNamed:@"alluser.png"];
    }
}

#pragma mark - AMBubbleTableDelegate

- (void)didSendText:(NSString*)text
{
    [self.data addObject:@{ @"text": text,
                            @"date": [NSDate date],
                            @"type": @(AMBubbleCellSent),
                            }];
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:(self.data.count - 1) inSection:0];
    [self.tableView beginUpdates];
    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationRight];
    [self.tableView endUpdates];
    // Either do this:
    [self scrollToBottomAnimated:YES];
    // or this:
    // [super reloadTableScrollingToBottom:YES];
}

- (NSString*)usernameForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return self.data[indexPath.row][@"username"];
}

- (UIColor*)usernameColorForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return self.data[indexPath.row][@"color"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    //[IQKeyboardManager sharedManager].enable = YES;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
