//
//  MySettingViewController.m
//  Gogou
//
//  Created by xijunli on 16/5/25.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MySettingViewController.h"
#import "MySettingTableViewCell.h"
#import "ImageProcess.h"
#import "ProfileTableViewController.h"
#import "AccountAndSecurityTableViewController.h"
#import "MainScreenViewController.h"

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface MySettingViewController ()<UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIButton *exitButton;


@end

@implementation MySettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self buttonInit];
    
    [self tableviewInit];
    
    [self setupNavigationBar];
    
}

- (void)tableviewInit{
    _tableView.separatorColor = BACKGROUND_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
    _tableView.backgroundColor = [UIColor clearColor];

}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
}

- (void)buttonInit{
    //[_exitButton.layer setBorderColor:(__bridge CGColorRef _Nullable)([UIColor grayColor])];
    //[_exitButton.layer setBorderWidth:1.0];
    [_exitButton setTitle:NSLocalizedString(@"退出登录", nil) forState:UIControlStateNormal];
    _exitButton.layer.borderColor = [UIColor grayColor].CGColor;
    _exitButton.layer.borderWidth = 0.5;
    _exitButton.layer.cornerRadius = 0.3;
    _exitButton.tintColor = BACKGROUND_CORLOR;
    [_exitButton addTarget:self action:@selector(exitButtonTap:) forControlEvents:UIControlEventTouchUpInside];
}

- (void) exitButtonTap:(id) sender{
    [self removeLoginInfo];
    MainScreenViewController *vc = [MainScreenViewController new];
    //[self.navigationController popToViewController:vc animated:YES];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void) removeLoginInfo{
    NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    static NSString *cellIdentifier = @"MySettingTableViewCell";
    
    if (indexPath.row == 0)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        MySettingTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        
        tmp.label.text = NSLocalizedString(@"个人资料", nil);
        cell = tmp;
    }
    
    if (indexPath.row == 1)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        MySettingTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        
        tmp.label.text = NSLocalizedString(@"账户与安全", nil);
        cell = tmp;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.row == 0){
        ProfileTableViewController *vc = [ProfileTableViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    if (indexPath.row == 1){
        AccountAndSecurityTableViewController *vc = [AccountAndSecurityTableViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }
}


-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
        if ([cell respondsToSelector:@selector(setSeparatorInset:)])
        {
            [cell setSeparatorInset:UIEdgeInsetsZero];
        }
        if ([cell respondsToSelector:@selector(setPreservesSuperviewLayoutMargins:)])
        {
            [cell setPreservesSuperviewLayoutMargins:NO];
        }
        if ([cell respondsToSelector:@selector(setLayoutMargins:)])
        {
            [cell setLayoutMargins:UIEdgeInsetsZero];
        }
    
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
