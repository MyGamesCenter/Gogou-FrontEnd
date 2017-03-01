//
//  ChangeBindingEmailViewController.m
//  Gogou
//
//  Created by xijunli on 16/6/28.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "ChangeBindingEmailViewController.h"
#import "FlatButton.h"
#import "ImageProcess.h"
#import "ChangeBindingEmailTableViewCell.h"
#import "Subscriber.h"
#import <RestKit/RestKit.h>
#import "GenericResponse.h"
#import "GoGouConstant.h"
#import "CacheManager.h"
#import "RESTRequestUtils.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface ChangeBindingEmailViewController ()<UITableViewDelegate,UITableViewDataSource>{
    UITextField *newEmailTextField;
    
}
@property (weak, nonatomic) IBOutlet UILabel *topLabel;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet FlatButton *button;
@property (nonatomic) Subscriber* subscriber;//登录者信息

@end

@implementation ChangeBindingEmailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self dataInit];
    [self tableviewInit];
    [self setupNavigationBar];
    [self buttonInit];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dataInit{
    _subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
}

- (void)tableviewInit{
    _tableView.separatorColor = BACKGROUND_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = NSLocalizedString(@"修改绑定邮箱", nil);
}

- (void)buttonInit{

    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_button setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    [_button setTitle:NSLocalizedString(@"下一步", nil) forState:UIControlStateNormal];
    [_button addTarget:self action:@selector(buttonTap:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)buttonTap:(id)sender{
    _subscriber.emailAddress = newEmailTextField.text;
    [RESTRequestUtils performUpdateSubsriberRequest:_subscriber delegate:self];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;

    static NSString *cellIdentifier = @"ChangeBindingEmailTableViewCell";
    
    if (indexPath.row == 0)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        ChangeBindingEmailTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (newEmailTextField == nil){
            tmp.keyLabel.text = NSLocalizedString(@"新邮箱", nil);
            newEmailTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入绑定邮箱地址", nil);
        }
    
        cell = tmp;
    }
    
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

#pragma mark - RESTRequestListener
- (void)onGogouRESTRequestSuccess:(id)result
{
    //alertClickFlag = true;
    
    UIAlertView *mBoxView =[[UIAlertView alloc]
                            initWithTitle:NSLocalizedString(@"绑定新邮箱成功！", nil)
                            message:nil
                            delegate:self
                            cancelButtonTitle:nil
                            otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
}

- (void)onGogouRESTRequestFailure
{
    //alertClickFlag = false;
    UIAlertView *mBoxView = [[UIAlertView alloc]
                             initWithTitle:NSLocalizedString(@"绑定新邮箱失败！", nil)
                             message:nil
                             delegate:self
                             cancelButtonTitle:nil
                             otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
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
