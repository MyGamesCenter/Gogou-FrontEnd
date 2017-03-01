//
//  MyAddressViewController.m
//  Gogou
//
//  Created by xijunli on 16/5/26.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MyAddressViewController.h"
#import "MyAddressTableViewCell.h"
#import "ImageProcess.h"
#import "FlatButton.h"
#import "AddAddressViewController.h"

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]
@interface MyAddressViewController ()<UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet FlatButton *button;



@end

@implementation MyAddressViewController

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
    //_tableView.scrollEnabled = false;
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = @"我的地址";
}

/*
- (void) setUpBottomButton{
    
    
    [_bottomButton setBackgroundImage:[self imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_bottomButton setBackgroundImage:[self imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    
    [_bottomButton setTitle:@"我要发布" forState:UIControlStateNormal];
    [_bottomButton addTarget:self action:@selector(bottomButtonTouchUpInside:) forControlEvents:UIControlEventTouchUpInside];
    
}
 */

- (void)buttonInit{
    //[_exitButton.layer setBorderColor:(__bridge CGColorRef _Nullable)([UIColor grayColor])];
    //[_exitButton.layer setBorderWidth:1.0];
    /*
    _exitButton.layer.borderColor = [UIColor grayColor].CGColor;
    _exitButton.layer.borderWidth = 0.5;
    _exitButton.layer.cornerRadius = 0.3;
    _exitButton.tintColor = BACKGROUND_CORLOR;
     */
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    [_button setTitle:NSLocalizedString(@"新建地址", nil) forState:UIControlStateNormal];
    [_button addTarget:self action:@selector(buttonTap:) forControlEvents:UIControlEventTouchUpInside];
}

- (void) buttonTap:(id) sender{
    AddAddressViewController *vc = [AddAddressViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 107;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 6;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 10;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    return 7;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    static NSString *cellIdentifier = @"MyAddressTableViewCell";
    
    UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
    [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
    
    MyAddressTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    
    cell = tmp;
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
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

@end
